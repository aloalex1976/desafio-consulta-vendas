package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	//@Query(nativeQuery = true, value = "SELECT tb_sales.ID, tb_sales.DATE, tb_sales.AMOUNT, tb_seller.NAME "
	//		+ "FROM tb_sales "
	//		+ "INNER JOIN tb_seller ON tb_seller.id = tb_sales.seller_id "
	//		+ "WHERE UPPER(tb_seller.name) LIKE '%ODINSON%' "
	//		+ "AND tb_sales.date BETWEEN '2024-01-01' AND '2025-03-28'")
	//List<SaleSellerProjection> search1();
	
	
	 @Query("""
		        SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(
		            s.id,
		            s.date,
		            s.amount,
		            s.seller.name
		        )
		        FROM Sale s
		        WHERE s.date BETWEEN :start AND :end
		          AND LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :name, '%')) 
		    """)
		    Page<SaleReportDTO> findSalesReport(
		            @Param("start") LocalDate start,
		            @Param("end") LocalDate end,
		            @Param("name") String sellerName,
		            Pageable pageable);


		    // SUMÁRIO
		    @Query("""
		        SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(
		            s.seller.name,
		            SUM(s.amount)
		        )
		        FROM Sale s
		        WHERE s.date BETWEEN :start AND :end
		        GROUP BY s.seller.name
		        
		    """)
		    List<SaleSummaryDTO> findSalesSummary(
		            @Param("start") LocalDate start,
		            @Param("end") LocalDate end);
		    

}
