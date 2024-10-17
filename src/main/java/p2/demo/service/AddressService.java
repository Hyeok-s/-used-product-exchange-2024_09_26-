package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.AddressDTO;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public void save(AddressDTO address, MemberEntity memberentity) {
        AddressEntity addressentity = new AddressEntity();
        addressentity.setPostcode(address.getPostcode());
        addressentity.setAddress(address.getAddress());
        addressentity.setDetailAddress(address.getDetailAddress());
        addressentity.setMember(memberentity);
        addressRepository.save(addressentity);
    }
}
