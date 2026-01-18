package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	
	public SaleService(SaleRepository repository) {
		
        this.repository = repository;
        
    }
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}
	
	
	private LocalDate getToday() {
		
		return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	}
	
	private LocalDate parseOrNull(String dateStr) {
		
		if (dateStr == null || dateStr.isBlank()) return null;
		
		return LocalDate.parse(dateStr);
	}
	
	private LocalDate[] resolveDates(String minDateStr, String maxDateStr) {

        LocalDate maxDate = parseOrNull(maxDateStr);
        LocalDate minDate = parseOrNull(minDateStr);

        if (maxDate == null) {
            maxDate = getToday();
        }

        if (minDate == null) {
            minDate = maxDate.minusYears(1L);
        }

        return new LocalDate[]{minDate, maxDate};
    }

    /* ----------------------------
       RELATÓRIO PAGINADO
     ---------------------------- */
    public Page<SaleReportDTO> getSalesReport(String minDateStr, String maxDateStr, String name, Pageable pageable) {

        LocalDate[] dates = resolveDates(minDateStr, maxDateStr);
        LocalDate min = dates[0];
        LocalDate max = dates[1];

        if (name == null) name = "";

        return repository.findSalesReport(min, max, name, pageable);
        
    }

    /* ----------------------------
       SUMÁRIO DE VENDAS
     ---------------------------- */
    public List<SaleSummaryDTO> getSalesSummary(String minDateStr, String maxDateStr) {

        LocalDate[] dates = resolveDates(minDateStr, maxDateStr);
        LocalDate min = dates[0];
        LocalDate max = dates[1];

        return repository.findSalesSummary(min, max);
    }
	
	
}
