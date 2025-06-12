package com.example.golf.service.impl;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.user.Request.ChangePasswordRequest;
import com.example.golf.dtos.user.Request.CreateUserRequest;
import com.example.golf.dtos.user.Response.UserResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.enums.GuestType;
import com.example.golf.enums.StaffRole;
import com.example.golf.enums.UserRole;
import com.example.golf.exception.AppException;
import com.example.golf.model.Guest;
import com.example.golf.model.Staff;
import com.example.golf.model.User;
import com.example.golf.repository.GuestRepository;
import com.example.golf.repository.StaffRepository;
import com.example.golf.repository.UserRepository;
import com.example.golf.service.UserService;
import com.example.golf.utils.SearchUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private SearchUtils<User> searchUtil;
    @Autowired
    private FileService fileService;


    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createStaff(CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new AppException(ErrorResponse.ENTITY_EXISTED);
        }
        User user = convertToEntity(request);
        user.setRole(request.getRole());
        user.setProvider("local");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        this.save(user);
        Optional<Staff> staff = staffRepository.findStaffByFullNameAndPhone(request.getFullName(), request.getPhone());
        if (staff.isPresent()) {
            staff.get().setUserId(user.getId());
            staffRepository.save(staff.get());
        }else {
            Staff newStaff = new Staff();
            newStaff.setFullName(request.getFullName());
            newStaff.setPhone(request.getPhone());
            newStaff.setEmail(request.getEmail());
            newStaff.setUserId(user.getId());
            if( request.getRole() == UserRole.STAFF) {
                newStaff.setRole(StaffRole.STAFF);
            } else if(request.getRole() == UserRole.ADMIN) {
                newStaff.setRole(StaffRole.MANAGER);
            }
            staffRepository.save(newStaff);
        }
        return convertToResponse(user, UserResponse.class);
    }

    @Transactional
    public UserResponse createGolfer(CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new AppException(ErrorResponse.ENTITY_EXISTED);
        }
        User user = convertToEntity(request);
        user.setRole(UserRole.MEMBER);
        user.setProvider("local");
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        this.save(user);
        Optional<Guest> guest = guestRepository.findGuestByFullNameAndPhone(request.getFullName(), request.getPhone());
        if (guest.isEmpty()) {
            Guest newGuest = new Guest();
            newGuest.setFullName(request.getFullName());
            newGuest.setUserId(user.getId());
            newGuest.setPhone(request.getPhone());
            newGuest.setEmail(request.getEmail());
            newGuest.setRole(GuestType.GOLFER);
            guestRepository.save(newGuest);
        } else {
            guest.get().setUserId(user.getId());
            guest.get().setEmail(request.getFullName());
            guest.get().setRole(GuestType.GOLFER);
            guestRepository.save(guest.get());
        }
        return convertToResponse(user, UserResponse.class);
    }

    @Override
    public UserResponse updateUser(String id, CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        User user = userOptional.get();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        this.save(user);
        return convertToResponse(user, UserResponse.class);
    }

    @Transactional
    public String softDelete(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        User user = userOptional.get();
        user.setActive(false);
        userRepository.save(user);
        return id;
    }

    public BaseSearchResponse<UserResponse> search(BaseSearchRequest request) {
        return searchUtil.search(User.class, request, (user) -> convertToResponse(user, UserResponse.class));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
    }

    @Override
    protected <Req> User convertToEntity(Req request) {
        return modelMapper.map(request, User.class);
    }

    @Override
    protected <Res> Res convertToResponse(User entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    public UserResponse getByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        User user = userOptional.get();
        return convertToResponse(user, UserResponse.class);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorResponse.INVALID_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public String changeAvatar(MultipartFile file) throws IOException {
        uploadFileAsync(file, getCurrentUser());
        return "Change avatar successfully";
    }



    @Async
    public void uploadFileAsync(MultipartFile file, User user) {
        try {
            String fileName = fileService.uploadFile(file); // upload file bình thường
            user.setAvatar(fileName);
            userRepository.save(user);
        } catch (IOException e) {
            // log error
        }
    }

}
