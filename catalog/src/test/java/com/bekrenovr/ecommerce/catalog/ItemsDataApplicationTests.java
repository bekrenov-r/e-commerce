package com.bekrenovr.ecommerce.catalog;

import com.bekrenovr.ecommerce.catalog.controller.ItemController;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.service.ItemService;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import org.apache.commons.lang.math.DoubleRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CatalogApplication.class})
class ItemsDataApplicationTests {

	ItemController itemController;
	ItemService itemService;

	ItemGenerator itemGenerator;

	@Autowired
	public ItemsDataApplicationTests(ItemController itemController, ItemService itemService, ItemGenerator itemGenerator) {
		this.itemController = itemController;
		this.itemService = itemService;
		this.itemGenerator = itemGenerator;
	}

	@Test
	void test() {
		DoubleRange dr = new DoubleRange(30.0, 100.0);
		System.out.println(dr.containsDouble(45.0));
	}

	@Test
	void generateItem(){
		Item item = itemGenerator.generateItem();
		System.out.println(item);
	}






}
