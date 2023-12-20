package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.domain.Sole;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByType(Integer type, Pageable pageable);
    @Query("SELECT c from  Product c where (CAST(c.id AS string) like :key " +
            "or c.code like concat('%',:key,'%') " +
            "or c.name like concat('%',:key,'%') " +
            "or c.brand.code like concat('%',:key,'%') " +
            "or c.brand.name like concat('%',:key,'%') " +
            "or c.sole.code like concat('%',:key,'%') " +
            "or c.sole.name like concat('%',:key,'%')) " +
            "and (:type is null or c.type = :type) ")
    Page<Product> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

    @Query(value ="SELECT c FROM Product c " +
            "outer JOIN ProductDetail pd on c.id = pd.product.id " +
            "WHERE ((CAST(c.id AS string) LIKE :key " +
            "OR CAST(pd.id AS string) LIKE :key " +
            "OR c.code LIKE CONCAT('%', :key, '%') " +
            "OR c.name LIKE CONCAT('%', :key, '%') " +
            "OR c.brand.code LIKE CONCAT('%', :key, '%') " +
            "OR c.brand.name LIKE CONCAT('%', :key, '%') " +
            "OR c.sole.code LIKE CONCAT('%', :key, '%') " +
            "OR c.sole.name LIKE CONCAT('%', :key, '%')" +
            "or :key is null or :key = '' ) " +
            "AND ( :type IS null OR c.type = :type) " +
            "AND (( :brandIDsSize = 0  OR c.brand.id IN :brandIDs) " +
            "AND (  :categoryIDsSize  = 0 OR c.category.id IN :categoryIDs) " +
            "AND (  :soleIDsSize  = 0 OR c.sole.id IN :soleIDs) " +
            "AND (  :colorIDsSize  = 0 OR pd.color.id IN :colorIDs) " +
            "AND (  :sizeIDsSize  = 0 OR pd.size.id IN :sizeIDs)) " +
            "AND ((SELECT COUNT(pd1) FROM ProductDetail pd1 WHERE pd1.product = c)=0 or(  (SELECT COUNT(pd1) FROM ProductDetail pd1 WHERE pd1.product = c and (:minPrice is null or pd1.price >=:minPrice)  and  (:maxPrice is null or  pd1.price <=:maxPrice) ) >0 ))) ")
    Page<Product> searchByKeyAndTypeAndFilter(
            @Param("key") String key,
            @Param("type") Integer type,
            @Param("brandIDs") List<UUID> brandIDs,
            @Param("brandIDsSize") Integer brandSize,
            @Param("categoryIDs") List<UUID> categoryIDs,
            @Param("categoryIDsSize") Integer categorySize,
            @Param("soleIDs") List<UUID> soleIDs,
            @Param("soleIDsSize") Integer soleIDsSize,
            @Param("colorIDs") List<UUID> colorIDs,
            @Param("colorIDsSize") Integer colorIDsSize,
            @Param("sizeIDs") List<UUID> sizeIDs,
            @Param("sizeIDsSize") Integer sizeIDsSize,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);


    @Query(value = "SELECT " +
            "product.id, " +
            "product.code, " +
            "product.productname, " +
            "MIN(productdetail.price) AS minPrice, " +
            "MAX(productdetail.price) AS maxPrice, " +
            "img.id AS imageId " +
            "FROM product " +
            "JOIN productdetail ON product.id = productdetail.productid " +
            "JOIN (SELECT " +
            "    id, " +
            "    productid, " +
            "    type, " +
            "    ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) AS RowNum " +
            "FROM images " +
            "WHERE type = 2) AS img ON product.id = img.productid AND img.RowNum = 1 " +
            "WHERE (:key IS NULL OR product.productname LIKE %:key%) " +
            "AND (:categoryId IS NULL OR product.categoryid = :categoryId) " +
            "AND (:productType IS NULL OR product.type = :productType) " +
            "GROUP BY product.id, product.code, product.productname, img.id", nativeQuery = true)
    List<ProductBanHangResponse> searchResponseByKeyAndType(@Param("key") String key, UUID categoryId, @Param("productType") Integer productType);



    @Query(value = "SELECT \n" +
            "    product.id,\n" +
            "    product.code,\n" +
            "    product.productname,\n" +
            "    MIN(productdetail.price) AS minPrice,\n" +
            "    MAX(productdetail.price) AS maxPrice,\n" +
            "    img.id AS imageId \n" +
            "FROM \n" +
            "    product \n" +
            "JOIN \n" +
            "    productdetail ON product.id = productdetail.productid \n" +
            "JOIN \n" +
            "    (SELECT \n" +
            "        id, \n" +
            "        productid,\n" +
            "        type,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) AS RowNum \n" +
            "    FROM \n" +
            "        images \n" +
            "    WHERE \n" +
            "        type = 2\n" +
            "    ) AS img ON product.id = img.productid AND img.RowNum = 1 \n" +
            "GROUP BY \n" +
            "    product.id, product.code, product.productname, img.id\n", nativeQuery = true)
    Page<ProductBanHangResponse> searchResponseByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);

    @Query(value = "SELECT p.id as id, p.code as code, p.productname as name, p.type as type, " +
            "p.description as description, p.create_date as createDate, c.code as categoryCode, c.category_name as categoryName, " +
            "s.code as soleCode, s.name as soleName, b.code as brandCode, b.name as brandName, " +
            "price.image_id as imageId, price.minPrice as priceMin, price.maxPrice as priceMax " +
            "FROM brand b join Product p on b.id = p.brandid\n" +
            "\t\t\t\t\tjoin sole s  on s.id = p.soleid\n" +
            "\t\t\t\t\tjoin categories c ON p.categoryid =c.id \n" +
            "\t\t\t\t\tleft join (select product.id as id, min(productdetail.price) as minPrice, max(productdetail.price) as maxPrice, images.id as image_id\n" +
            "\t\t\t\t\tfrom product\n" +
            "\t\t\t\t\tjoin productdetail on product.id = productdetail.productid\n" +
            "\t\t\t\t\tjoin (\n" +
            "\t\t\t\t\t\tSELECT id, productid, type, ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) as RowNum\n" +
            "\t\t\t\t\t\tFROM images\n" +
            "\t\t\t\t\t\twhere images.type = 2\n" +
            "\t\t\t\t\t) images on product.id = images.productid\n" +
            "\t\t\t\t\twhere images.RowNum = 1\n" +
            "\t\t\t\t\tGROUP BY product.id,images.id\n" +
            "\t\t\t\t\t) price on price.id = p.id\n" +
            "WHERE (\n" +
            " :key is null or\n" +
            "    CAST(p.id AS NVARCHAR(MAX)) LIKE :key \n" +
            "    OR p.code LIKE '%' + :key + '%'\n" +
            "    OR p.productname LIKE '%' + :key + '%'\n" +
            "    OR b.code LIKE '%' + :key + '%'\n" +
            "    OR b.name LIKE '%' + :key + '%'\n" +
            "    OR c.code LIKE '%' + :key + '%'\n" +
            "    OR c.category_name LIKE '%' + :key + '%'\n" +
            "    OR s.code LIKE '%' + :key + '%'\n" +
            "    OR s.name LIKE '%' + :key + '%')\n" +
            "    AND (:type IS NULL OR p.type = :type);",
            countQuery = "SELECT count(p.id)  " +
                    "FROM brand b join Product p on b.id = p.brandid\n" +
                    "\t\t\t\t\tjoin sole s  on s.id = p.soleid\n" +
                    "\t\t\t\t\tjoin categories c ON p.categoryid =c.id \n" +
                    "\t\t\t\t\tleft join (select product.id as id, min(productdetail.price) as minPrice, max(productdetail.price) as maxPrice, images.id as image_id\n" +
                    "\t\t\t\t\tfrom product\n" +
                    "\t\t\t\t\tjoin productdetail on product.id = productdetail.productid\n" +
                    "\t\t\t\t\tjoin (\n" +
                    "\t\t\t\t\t\tSELECT id, productid, type, ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) as RowNum\n" +
                    "\t\t\t\t\t\tFROM images\n" +
                    "\t\t\t\t\t\twhere images.type = 2\n" +
                    "\t\t\t\t\t) images on product.id = images.productid\n" +
                    "\t\t\t\t\twhere images.RowNum = 1\n" +
                    "\t\t\t\t\tGROUP BY product.id,images.id\n" +
                    "\t\t\t\t\t) price on price.id = p.id\n" +
                    "WHERE (\n" +
                    " :key is null or\n" +
                    "    CAST(p.id AS NVARCHAR(MAX)) LIKE :key \n" +
                    "    OR p.code LIKE '%' + :key + '%'\n" +
                    "    OR p.productname LIKE '%' + :key + '%'\n" +
                    "    OR b.code LIKE '%' + :key + '%'\n" +
                    "    OR b.name LIKE '%' + :key + '%'\n" +
                    "    OR c.code LIKE '%' + :key + '%'\n" +
                    "    OR c.category_name LIKE '%' + :key + '%'\n" +
                    "    OR s.code LIKE '%' + :key + '%'\n" +
                    "    OR s.name LIKE '%' + :key + '%')\n" +
                    "    AND (:type IS NULL OR p.type = :type);",
            nativeQuery = true)
    Page<ProductResponse> searchResponseByKeyAndTypeAndFilter(@Param("key") String key, @Param("type") Integer type, Pageable pageable);


    Optional<Product> findByCode(String code);

}
