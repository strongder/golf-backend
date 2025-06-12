package com.example.golf.service;

import com.example.golf.dtos.membership.request.CreateMembershipRequest;
import com.example.golf.dtos.membership.request.MembershipSearchRequest;
import com.example.golf.dtos.membership.response.MembershipResponse;
import com.example.golf.dtos.membership_type.response.MembershipTypeResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.model.Membership;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MembershipService extends BaseService<Membership, String> {

    MembershipResponse registerMembership(CreateMembershipRequest request);
    String confirmMembership(String membershipId);

    String inActiveMemberShip(String membershipId);

    String cancelMembership(String membershipId);
    String softDelete(String id);
    MembershipResponse renewMembership(String membershipId);
    List<MembershipResponse> getMembershipHistory(String userId);
    BaseSearchResponse<MembershipResponse> search(MembershipSearchRequest request);

    List<MembershipResponse> getByUserId(String userId);

    List<MembershipResponse> getByUserAndStatus(String userId, MembershipStatus status);

    // lay membership gan nhat
    MembershipResponse getMembershipLatest(String userId);
}
