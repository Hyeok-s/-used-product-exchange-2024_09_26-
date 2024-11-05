package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.AddressDTO;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.AddressRepository;

import java.util.List;

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

    public List<AddressEntity> getAddressesByMemberId(Long memberId) {
        return addressRepository.findByMemberId(memberId);
    }


    //id가져오기
    public AddressEntity findById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }

    // 주소 수정 메서드
    public void updateAddress(AddressDTO addressDTO) {
        AddressEntity addressEntity = findById(addressDTO.getId());
        addressEntity.setPostcode(addressDTO.getPostcode());
        addressEntity.setAddress(addressDTO.getAddress());
        addressEntity.setDetailAddress(addressDTO.getDetailAddress());
        addressRepository.save(addressEntity);
    }

    // 주소 삭제 메서드
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    public void deleteByMemberId(Long memberId) {
        addressRepository.deleteByMemberId(memberId);
    }
}
