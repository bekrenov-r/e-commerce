package com.bekrenovr.ecommerce.catalog.util.sort;

import com.bekrenovr.ecommerce.catalog.dto.response.UniqueItemShortResponse;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.SizeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class UniqueItemBySizeComparator implements Comparator<UniqueItemShortResponse> {
    private final SizeFactory sizeFactory;
    @Override
    public int compare(UniqueItemShortResponse item1, UniqueItemShortResponse item2) {
        Size size1 = sizeFactory.getSize(item1.size());
        Size size2 = sizeFactory.getSize(item2.size());
        return new SizeComparator().compare(size1, size2);
    }
}
