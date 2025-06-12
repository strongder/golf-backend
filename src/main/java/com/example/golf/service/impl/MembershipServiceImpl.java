package com.example.golf.service.impl;

import com.example.golf.dtos.membership.request.CreateMembershipRequest;
import com.example.golf.dtos.membership.request.MembershipSearchRequest;
import com.example.golf.dtos.membership.response.MembershipResponse;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.enums.NotificationType;
import com.example.golf.exception.AppException;
import com.example.golf.model.Membership;
import com.example.golf.model.MembershipType;
import com.example.golf.model.Notification;
import com.example.golf.model.User;
import com.example.golf.repository.MemberShipRepository;
import com.example.golf.repository.MemberShipTypeRepository;
import com.example.golf.repository.MembershipRepositoryImpl;
import com.example.golf.repository.UserRepository;
import com.example.golf.service.MembershipService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class MembershipServiceImpl extends BaseServiceImpl<Membership, String> implements MembershipService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SearchUtils<Membership> searchUtils;
    @Autowired
    public MemberShipRepository memberShipRepository;
    @Autowired
    private MembershipRepositoryImpl repositoryImpl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberShipTypeRepository memberShipTypeRepository;
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    public MembershipServiceImpl(MemberShipRepository memberShipRepository, ModelMapper modelMapper) {
        super(memberShipRepository);
        this.memberShipRepository = memberShipRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    protected <Req> Membership convertToEntity(Req request) {
        return modelMapper.map(request, Membership.class);
    }

    @Override
    protected <Res> Res convertToResponse(Membership entity, Class<Res> responseType) {
        User user = userRepository.findById(entity.getUserId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        MembershipType membershipType = memberShipTypeRepository.findById(entity.getMembershipTypeId()).orElseThrow(
                () -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED)
        );
        MembershipResponse response = modelMapper.map(entity, MembershipResponse.class);

        response.setMembershipTypeName(membershipType.getName());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setDiscount(membershipType.getDiscount());
        response.setPhone(user.getPhone());
        return responseType.cast(response);
    }

    @Override
    @Transactional
    public MembershipResponse registerMembership(CreateMembershipRequest request) {
        userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        MembershipType membershipType = memberShipTypeRepository.findById(request.getMembershipTypeId())
                .orElseThrow(() -> new RuntimeException("Membership type not found"));
        Membership membership = new Membership();
        membership.setCode(generateMembershipCode());
        membership.setUserId(request.getUserId());
        membership.setMembershipTypeId(membershipType.getId());
        membership.setPrice(membershipType.getPrice());
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(membershipType.getDuration()));
        membership.setStatus(MembershipStatus.PENDING);
        memberShipRepository.save(membership);
        return convertToResponse(membership, MembershipResponse.class);
    }

    @Override
    @Transactional
    public String confirmMembership(String membershipId) {
        Membership membership = memberShipRepository.findById(membershipId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        membership.setStatus(MembershipStatus.ACTIVE);
        memberShipRepository.save(membership);
        return membership.getId();
    }

    @Transactional
    @Override
    public String inActiveMemberShip(String membershipId) {
        Membership membership = memberShipRepository.findById(membershipId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        membership.setStatus(MembershipStatus.INACTIVE);
        memberShipRepository.save(membership);
        return membership.getId();
    }

    @Override
    @Transactional
    public String cancelMembership(String membershipId) {
        Membership membership = memberShipRepository.findById(membershipId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        membership.setStatus(MembershipStatus.CANCELLED);
        memberShipRepository.save(membership);
        return membership.getId();
    }

    @Override
    @Transactional
    public String softDelete(String id) {
        Membership membership = memberShipRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        membership.setDeleted(true);
        memberShipRepository.save(membership);
        return membership.getId();
    }

    @Override
    @Transactional
    public MembershipResponse renewMembership(String membershipId) {
        Membership membership = memberShipRepository.findById(membershipId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        MembershipType membershipType = memberShipTypeRepository.findById(membership.getMembershipTypeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));

        membership.setStartDate(LocalDate.now());
        membership.setEndDate(membership.getEndDate().plusMonths(membershipType.getDuration()));
        memberShipRepository.save(membership);
        return convertToResponse(membership, MembershipResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipResponse> getMembershipHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        List<Membership> memberships = memberShipRepository.findByUserId(user.getId());
        return memberships.stream()
                .map(membership -> convertToResponse(membership, MembershipResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<MembershipResponse> search(MembershipSearchRequest request) {
        return repositoryImpl.searchMembership(request);
    }

    @Override
    @Transactional
    public List<MembershipResponse> getByUserId(String userId) {
        List<Membership> memberShip = memberShipRepository.findByUser(userId);
        return memberShip.stream()
                .map(m -> convertToResponse(m, MembershipResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<MembershipResponse> getByUserAndStatus(String userId, MembershipStatus status) {
        List<Membership> memberShip = memberShipRepository.findByUserIdAndStatus(userId, status);
        if (memberShip == null) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        return memberShip.stream()
                .map(m -> convertToResponse(m, MembershipResponse.class))
                .toList();
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void checkMembershipExpiration() {
        LocalDate today = LocalDate.now();
        List<Membership> memberships = memberShipRepository.findAll();
        for (Membership membership : memberships) {
            if (membership.getEndDate().isBefore(today) && membership.getStatus() == MembershipStatus.ACTIVE) {
                membership.setStatus(MembershipStatus.EXPIRED);
                memberShipRepository.save(membership);
            }
        }
    }

    public void cancelAllMembershipPending(String userId, MembershipStatus status)
    {
        List<Membership> memberships = memberShipRepository.findByUserIdAndStatus(userId, status);
        if (memberships.isEmpty()) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        for (Membership membership : memberships) {
            membership.setStatus(MembershipStatus.CANCELLED);
            memberShipRepository.save(membership);
        }
    }

    // lay membership gan nhat
    @Override
    public MembershipResponse getMembershipLatest(String userId) {
        Membership membership = memberShipRepository.findByMembershipLatest(userId);
        if (membership==null) {
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        }
        return convertToResponse(membership, MembershipResponse.class);
    }

    public String generateMembershipCode() {

        return "MEM" + String.format("%04d", memberShipRepository.count() + 1);
    }

    public void changeStatus(String referenceId, MembershipStatus membershipStatus) {
        Membership membership = memberShipRepository.findById(referenceId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        membership.setStatus(membershipStatus);
        memberShipRepository.save(membership);
    }

    // conjob membership sap het han de thong bao toi user
    @Scheduled(cron = "0 0 1 * * ?") // Mỗi ngày lúc 0 giờ sáng
    public void sendNotificationToUserAboutMembershipExpiration() {
        LocalDate today = LocalDate.now();
        List<Membership> memberships = memberShipRepository.findAll();
        for (Membership membership : memberships) {
            if (membership.getEndDate().isEqual(today) && membership.getStatus() == MembershipStatus.ACTIVE) {
                User user = userRepository.findById(membership.getUserId())
                        .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                Notification notification = Notification.builder()
                        .userId(user.getId())
                        .type(NotificationType.MEMBERSHIP)
                        .isRead(false)
                        .dataId(membership.getId())
                        .title("Thông báo hết hạn thành viên")
                        .content("Thành viên của bạn sẽ hết hạn vào ngày " + membership.getEndDate() + ". Vui lòng gia hạn để tiếp tục sử dụng dịch vụ.")
                        .build();
                notificationServiceImpl.sendNotification(notification);
            }
        }
    }

}
