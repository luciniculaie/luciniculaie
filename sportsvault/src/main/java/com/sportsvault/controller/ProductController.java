package com.sportsvault.controller;

import com.sportsvault.dto.ProductDTO;
import com.sportsvault.mapper.ProductMapper;
import com.sportsvault.model.Product;
import com.sportsvault.model.ProductAttributeValue;
import com.sportsvault.model.ProductQuery;
import com.sportsvault.model.ProductState;
import com.sportsvault.repository.AttributeRepository;
import com.sportsvault.repository.PhotoRepository;
import com.sportsvault.repository.ProductAttributeValueRepository;
import com.sportsvault.repository.ProductRepository;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final PhotoRepository photoRepository;

    public ProductController(ProductMapper productMapper, ProductRepository productRepository,
                             AttributeRepository attributeRepository,
                             ProductAttributeValueRepository productAttributeValueRepository,
                             PhotoRepository photoRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.attributeRepository = attributeRepository;
        this.productAttributeValueRepository = productAttributeValueRepository;
        this.photoRepository = photoRepository;
    }

    @GetMapping("/photos/{id}")
    List<String> getProductPhotos(@PathVariable("id") UUID id) {
        return photoRepository.getProductPhotos(id);
    }

    @PostMapping("/addProduct")
    UUID addProduct(@RequestBody ProductDTO productDTO) {
        Product toSave = productMapper.toEntity(productDTO);
        List<ProductAttributeValue> productAttributes = toSave.getAttributeValues();
        toSave.setAttributeValues(null);
        Product newProduct = productRepository.save(toSave);
        productAttributes = productAttributes.stream().peek((productAttributeValue ->
                productAttributeValue.setProduct(Product.builder().id(newProduct.getId()).build()))).collect(Collectors.toList());
        productAttributeValueRepository.saveAll(productAttributes);
        return newProduct.getId();
    }

    @PostMapping("/updateexpired")
    ProductDTO updateExpired(@RequestBody ProductDTO productDTO) {
        Product toUpdate = productMapper.toEntity(productDTO);
        toUpdate.setState(ProductState.SELLING);
        return productMapper.toDto(productRepository.save(toUpdate));
    }

    @GetMapping("/{id}")
    ProductDTO findById(@PathVariable("id") UUID id) {
        return productMapper.toDto(productRepository.findById(id).get());
    }

    @GetMapping("/getIds")
    List<UUID> getAllIds() {
        return productRepository.getAllIds();
    }

    @GetMapping("/all")
    List<ProductDTO> getProductList() {
        return productRepository.getProductList().get().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    public static List<String> getAllCombinations(Collection<Set<String>> input) {
        List<String> combinations = new ArrayList<>();
        generateCombinations(input, 0, new HashSet<>(), combinations);
        return combinations;
    }

    private static void generateCombinations(Collection<Set<String>> input, int index, Set<String> current, List<String> combinations) {
        if (index == input.size()) {
            combinations.add(String.join(" ", current));
            return;
        }

        Set<String> words = getSetAtIndex(input, index);
        for (String word : words) {
            current.add(word);
            generateCombinations(input, index + 1, current, combinations);
            current.remove(word);
        }
    }

    private static Set<String> getSetAtIndex(Collection<Set<String>> collection, int index) {
        int currentIndex = 0;
        for (Set<String> set : collection) {
            if (currentIndex == index) {
                return set;
            }
            currentIndex++;
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }

    @GetMapping("/search/{search}")
    List<ProductDTO> searchProducts(@PathVariable("search") String search) {
        int maximumEditDistance = 2;

        String[] searchTermsArray = search.split(" ");
        List<String> searchTerms = Arrays.asList(searchTermsArray);
        List<Product> allProducts = productRepository.getProductList().get();

        Map<String, Set<String>> searchMap = new HashMap<>();
        for (String term : searchTerms) {
            Set<String> termSet = new HashSet<>();
            termSet.add(term);
            boolean termFlag = false;
            for (Product product : allProducts) { // Iterate over all products to compare against
                String[] productWordsArray = product.getName().split(" "); // Split product name into individual words
                List<String> productWords = Arrays.asList(productWordsArray);
                for (String word : productWords) {
                    Integer distance = LevenshteinDistance.getDefaultInstance().apply(term.toLowerCase(), word.toLowerCase());
                    if (distance <= maximumEditDistance) {
                        termSet.add(word.toLowerCase());
                    }
                    if(distance == 0) {
                        termFlag = true;
                        break;
                    }
                }
                if(termFlag) {
                    termSet = new HashSet<>();
                    termSet.add(term);
                    break;
                }
            }
            searchMap.put(term, termSet);
        }

        List<String> combinations = getAllCombinations(searchMap.values());
        List<String> expressions = combinations.stream()
                .map(combination -> Arrays.stream(combination.split(" "))
                        .map(keyword -> "+" + keyword.toLowerCase() + "*")
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.toList());

        Set<UUID> uuidSet = new HashSet<>();
        for(String expression : expressions) {
            Optional<List<UUID>> returnedUUIDs = productRepository.searchProducts(expression);
            returnedUUIDs.ifPresent(uuidSet::addAll);
        }

        return productRepository.findAllById(uuidSet).stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") UUID id) {
        productRepository.deleteById(id);
    }

    @PostMapping("/create")
    Product createProduct(@RequestBody Product product) {
        return null;
    }

    @GetMapping("/gender/{gender}/sport/{sport}")
    List<ProductDTO> getProductListByGenderAndSport(@PathVariable("gender") String gender, @PathVariable("sport") String sport) {
        String capitalizedSport = sport.substring(0, 1).toUpperCase() + sport.substring(1);
        String capitalizedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1);
        System.out.println(productRepository.getProductListByGenderAndSport(capitalizedGender, capitalizedSport).get().stream().map(productMapper::toDto).collect(Collectors.toList()));
        return productRepository.getProductListByGenderAndSport(capitalizedGender, capitalizedSport).get().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/wonproducts")
    @PreAuthorize("hasRole('USER')")
    List<ProductDTO> wonProducts() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return productRepository.getWonProducts(userDetails.getId()).stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/myproducts")
    @PreAuthorize("hasRole('USER')")
    List<ProductDTO> myProducts() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return productRepository.getMyProducts(userDetails.getId()).stream().map(productMapper::toDto).collect(Collectors.toList());
    }


    @GetMapping("/gender/{gender}")
    List<ProductDTO> getProductListByGender(@PathVariable("gender") String gender) {
        String capitalizedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1);
        return productRepository.getProductListByGender(capitalizedGender).get().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/gender/{gender}/category/{category}")
    List<ProductDTO> getProductListByGenderAndCategory(@PathVariable("gender") String gender, @PathVariable("category") String category) {
        String[] categoryStringList = category.split(" ");
        StringBuilder capitalizedCategory = new StringBuilder();
        for(int i = 0; i < categoryStringList.length; i++)
            if(i == categoryStringList.length - 1)
                capitalizedCategory.append(categoryStringList[i].substring(0, 1).toUpperCase()).append(categoryStringList[i].substring(1));
            else
                capitalizedCategory.append(categoryStringList[i].substring(0, 1).toUpperCase()).append(categoryStringList[i].substring(1)).append(" ");

        System.out.println(capitalizedCategory.toString());

        String capitalizedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1);
        return productRepository.getProductListByGenderAndCategory(capitalizedGender, capitalizedCategory.toString()).get().stream().map(productMapper::toDto).collect(Collectors.toList());
    }


}
