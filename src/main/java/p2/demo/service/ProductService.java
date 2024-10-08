package p2.demo.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.ProductEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.repository.MemberRepository;
import p2.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void saveProduct(ProductDTO productDTO, MultipartFile pic, HttpSession session) throws IOException {
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            logger.info("Saving product: {}", productDTO.toString());

            ProductEntity productEntity = new ProductEntity();
            productEntity.setPName(productDTO.getPName());
            productEntity.setPContent(productDTO.getPContent());
            productEntity.setPPrice(productDTO.getPPrice());
            productEntity.setPType(productDTO.getPType());
            Long memberId = loggedInUser.getId();
            MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);

            if (memberEntity != null) {
                productEntity.setMember(memberEntity);

                // 랜덤한 이름으로 파일 저장
                if (pic != null && !pic.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + pic.getOriginalFilename();
                    Path savePath = Paths.get(uploadDir + File.separator + fileName);

                    // 디렉토리 없으면 생성
                    if (!Files.exists(Paths.get(uploadDir))) {
                        Files.createDirectories(Paths.get(uploadDir));
                    }

                    pic.transferTo(savePath.toFile());

                    productEntity.setPic(fileName);  // 파일 이름 저장
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

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> getProductsByType(String type) {
        return productRepository.findByPtype(type);
    }


}
