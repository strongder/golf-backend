package com.example.golf.service.impl;

import com.example.golf.dtos.guest.request.UpdateGuestRequest;
import com.example.golf.dtos.guest.response.GuestResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.Guest;
import com.example.golf.model.User;
import com.example.golf.repository.GuestRepository;
import com.example.golf.repository.UserRepository;
import com.example.golf.service.GuestService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GuestServiceImpl extends BaseServiceImpl<Guest, String> implements GuestService {

    @Autowired
    GuestRepository guestRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private SearchUtils<Guest> searchUtil;
    @Autowired
    private UserRepository userRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        super(guestRepository);
        this.guestRepository = guestRepository;
    }

    public BaseSearchResponse<GuestResponse> search(BaseSearchRequest request) {
        return searchUtil.search(Guest.class, request, (guest) -> convertToResponse(guest, GuestResponse.class));
    }

    @Override
    public String softDelete(String id) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        guest.setDeleted(true);
        guestRepository.save(guest);
        return id;
    }

    @Transactional
    @Override
    public GuestResponse updateGuest(String id, UpdateGuestRequest request) {
        Guest existingGuest = guestRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        // check email
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent())
        {
            if (!existingUser.get().getEmail().equals(existingGuest.getEmail())) {
                throw new AppException(ErrorResponse.ENTITY_EXISTED);
            }
        }
        modelMapper.map(request, existingGuest);
        Guest saveGuest = guestRepository.save(existingGuest);
        // Update user information if provided
        if (saveGuest.getUserId() != null) {
            User user = userRepository.findById(saveGuest.getUserId())
                    .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
//            user.setEmail(request.getEmail());
            userRepository.save(user);
        }
        return convertToResponse(saveGuest, GuestResponse.class);
    }

    @Override
    public GuestResponse getGuestByUserId(String userId) {
        Guest guest = guestRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        return convertToResponse(guest, GuestResponse.class);
    }

    @Override
    protected Guest convertToEntity(Object request) {
        return null;
    }

    @Override
    protected <Res> Res convertToResponse(Guest entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }
}
