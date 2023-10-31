package com.fpt.duantn.repository;

import com.fpt.duantn.domain.Brand;
import com.fpt.duantn.domain.Color;
import com.fpt.duantn.domain.Product;
import com.fpt.duantn.dto.ProductBanHangResponse;
import com.fpt.duantn.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
            "and (:type is null or c.type = :type)")
    Page<Product> searchByKeyAndType(@Param("key") String key, @Param("type") Integer type, Pageable pageable);



    @Query(value = "select product.id,product.code,product.productname,min(productdetail.price) 'minPrice' ,max(productdetail.price) 'maxPrice' ,images.id 'imageId'  from product \n" +
            " join productdetail on product.id = productdetail.productid \n" +
            "join (SELECT id, productid,type,ROW_NUMBER() OVER (PARTITION BY productid ORDER BY NEWID()) as RowNum \n" +
            "FROM images \n" +
            "where images.type =2 ) images on product.id = images.productid\n" +
            "where images.RowNum =1 \n" +
            "GROUP BY product.id,product.code,product.productname,images.id ", nativeQuery = true)
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


}
