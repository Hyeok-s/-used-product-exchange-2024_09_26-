package p2.demo.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.ProductEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p2.demo.entity.OrderEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishlistRepository wishlistRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    //새로운 상품 저장
    @Transactional
    public void saveProduct(ProductDTO productDTO, MultipartFile pic, MultipartFile pic1, MultipartFile pic2, HttpSession session) throws IOException {
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            logger.info("Saving product: {}", productDTO.toString());

            ProductEntity productEntity = new ProductEntity();
            productEntity.setPName(productDTO.getPName());
            productEntity.setPContent(productDTO.getPContent());
            productEntity.setPPrice(productDTO.getPPrice());
            productEntity.setPType1(productDTO.getPType1());
            productEntity.setPType2(productDTO.getPType2());
            productEntity.setPTime(LocalDateTime.now());
            Long memberId = loggedInUser.getId();
            MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);

            if (memberEntity != null) {
                productEntity.setMember(memberEntity);

                // 랜덤한 이름으로 파일 저장
                if (pic != null && !pic.isEmpty()) {
                    String fileName = saveFile(pic);
                    productEntity.setPic(fileName);
                }
                else {
                    String fileName = "nothing.png";
                    Path defaultImagePath = Paths.get(uploadDir + File.separator + fileName);
                    // 기본 이미지가 없으면 예외 처리
                    if (!Files.exists(defaultImagePath)) {
                        throw new IOException("Default image 'nothing.png' not found in upload directory.");
                    }
                    productEntity.setPic(fileName);
                }
                if (pic1 != null && !pic1.isEmpty()) {
                    String fileName1 = saveFile(pic1);
                    productEntity.setPic1(fileName1);
                }
                if (pic2 != null && !pic2.isEmpty()) {
                    String fileName2 = saveFile(pic2);
                    productEntity.setPic2(fileName2);
                }

                productRepository.save(productEntity);
            } else {
                logger.warn("No User found with ID: {}", memberId);
                throw new IllegalStateException("No User found");
            }
        } else {
            logger.warn("User is not logged in.");
            throw new IllegalStateException("User is not logged in.");
        }
    }



    //사진 파일 저장
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadDir + File.separator + fileName);

        if (!Files.exists(savePath.getParent())) {
            Files.createDirectories(savePath.getParent());
        }

        file.transferTo(savePath.toFile());
        return fileName;
    }

    //상품 종류 가져오기
    public List<ProductEntity> getProductsByType1(String type1) {
        return productRepository.findBypType1(type1);
    }

    public List<ProductEntity> getProductsByType2(String type2) {
        return productRepository.findBypType2(type2);
    }


    //상품 상태가져오기
    public List<ProductEntity> getProductsByState(String state){
        return productRepository.findBypState(state);
    }

    //아이디로 가져오기
    public ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }

    // 상태 업데이트
    public void updateProductState(Long id, String newState) {
        ProductEntity product = findById(id);
        product.setPState(newState);  // 상태 변경
        product.setPTime(LocalDateTime.now());
        productRepository.save(product);
    }

    //상태에 따라 상품 가져오기
    public List<ProductEntity> getpState(String state){
        return productRepository.findBypState(state);
    }

    //로그인 id와 상태로 가져오기
    public List<ProductEntity> getProductsByStatusAndMemberIdAndFalse(String state, Long memberId) {
        return productRepository.findBypStateAndMemberIdAndTrash(state, memberId, false);
    }

    //상품정보 업데이트
    public void updateProduct(ProductDTO productDTO, MultipartFile pic, MultipartFile pic1, MultipartFile pic2) throws IOException {

        ProductEntity productEntity = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        productEntity.setPName(productDTO.getPName());
        productEntity.setPContent(productDTO.getPContent());
        productEntity.setPPrice(productDTO.getPPrice());
        productEntity.setPType1(productDTO.getPType1());
        productEntity.setPType2(productDTO.getPType2());
        productEntity.setCounts(0);
        productEntity.setPState("N");

        //사진
        if (pic != null && !pic.isEmpty()) {
            //이전 사진 삭제
            String oldFileName = productEntity.getPic();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                Path oldFilePath = Paths.get(uploadDir + File.separator + oldFileName);
                Files.deleteIfExists(oldFilePath);  // 기존 파일 삭제
            }

            // 랜덤한 이름으로 파일 저장
            String fileName = UUID.randomUUID() + "_" + pic.getOriginalFilename();
            Path savePath = Paths.get(uploadDir + File.separator + fileName);

            // 디렉토리 없으면 생성
            if (!Files.exists(Paths.get(uploadDir))) {
                Files.createDirectories(Paths.get(uploadDir));
            }

            pic.transferTo(savePath.toFile());

            productEntity.setPic(fileName);  // 파일 이름 저장
        }
        if (pic1 != null && !pic1.isEmpty()) {
            String oldFileName = productEntity.getPic1();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                Path oldFilePath = Paths.get(uploadDir + File.separator + oldFileName);
                Files.deleteIfExists(oldFilePath);  // 기존 파일 삭제
            }
            String fileName1 = saveFile(pic1);
            productEntity.setPic1(fileName1);
        }
        if (pic2 != null && !pic2.isEmpty()) {
            String oldFileName = productEntity.getPic2();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                Path oldFilePath = Paths.get(uploadDir + File.separator + oldFileName);
                Files.deleteIfExists(oldFilePath);  // 기존 파일 삭제
            }
            String fileName2 = saveFile(pic2);
            productEntity.setPic2(fileName2);
        }
        productRepository.save(productEntity);

    }


    // 조회수 추가
    public void incrementProductCount(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
        productEntity.setCounts(productEntity.getCounts()+1);
        productRepository.save(productEntity);
    }

    public boolean hasProductsWithStateC(Long memberId) {
        List<ProductEntity> products = productRepository.findBypStateAndMemberId("C", memberId);
        return !products.isEmpty();
    }

    @Transactional
    public void updateMemberIdToNull(Long memberId) {
        // products의 memberId를 null로 업데이트
        productRepository.updateMemberIdToNull(memberId);
    }
}
