package ensa.ma.Api.service;

import ensa.ma.Api.model.Category;
import ensa.ma.Api.model.Product;

import ensa.ma.Api.model.dataTransferObjects.ProductDto;
import ensa.ma.Api.repos.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    public List<ProductDto> listProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products) {
            ProductDto productDto = getDtoFromProduct(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public List<ProductDto> listProductsByWord(String keyWord){
      List<Product> products = productRepository.searchByTitleLike(keyWord);
      List<ProductDto> productDtos = new ArrayList<>();
      for(Product product : products) {
        ProductDto productDto = getDtoFromProduct(product);
        productDtos.add(productDto);
      }
      return productDtos;
    }

  public List<ProductDto> listProductsByCategoryId(Long categoryId){
    List<Product> products = productRepository.findByCategoryId(categoryId);
    List<ProductDto> productDtos = new ArrayList<>();
    for(Product product : products) {
      ProductDto productDto = getDtoFromProduct(product);
      productDtos.add(productDto);
    }
    return productDtos;
  }

    public static ProductDto getDtoFromProduct(Product product) {
        ProductDto productDto = new ProductDto(product);
        return productDto;
    }

    public static Product getProductFromDto(ProductDto productDto, Category category) {
        Product product = new Product(productDto, category);
        return product;
    }

    public void addProduct(ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public void updateProduct(Long productID, ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        product.setId(productDto.getId());
        productRepository.save(product);
    }


    public Product getProductById(Long productId) throws IOException {
        Optional<Product> optionalProduct = (productRepository.findById(productId));
        if (!optionalProduct.isPresent())
            throw new IOException();
        return optionalProduct.get();
    }

    public Optional<ProductDto> getProduct(Long productID) {
      Optional<Product> product = productRepository.findById(productID);
      Optional<ProductDto> productDto = Optional.of(getDtoFromProduct(product.get()));
      return productDto;
  }


    public void deleteByID(Long productID) {
        productRepository.deleteById(productID);
    }
}
