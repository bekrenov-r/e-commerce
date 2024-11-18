package com.bekrenovr.ecommerce.catalog.item.sorting;

import com.bekrenovr.ecommerce.catalog.item.size.Size;
import com.bekrenovr.ecommerce.catalog.item.size.SizeFactory;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class UniqueItemBySizeComparator implements Comparator<UniqueItemDTO> {
    private final SizeFactory sizeFactory;
    @Override
    public int compare(UniqueItemDTO item1, UniqueItemDTO item2) {
        Size size1 = sizeFactory.getSize(item1.size());
        Size size2 = sizeFactory.getSize(item2.size());
        return new SizeComparator().compare(size1, size2);
    }
}
