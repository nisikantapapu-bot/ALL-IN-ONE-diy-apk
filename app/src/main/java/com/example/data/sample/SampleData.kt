package com.example.data.sample

import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity

data class DeliveryLocationPreset(
    val name: String,
    val distanceKm: Double,
    val landmark: String,
    val isWithinRadius: Boolean
)

object SampleData {

    val defaultStoreConfig = StoreConfigEntity(
        id = 1,
        storeName = "ALL IN ONE",
        tagline = "Apna Supermarket & Express Delivery",
        address = "Ragadi Bazar, Jajpur Road, Odisha - 755019",
        storeLat = 20.9507,
        storeLng = 86.1344,
        phone = "+919437123456",
        whatsappPhone = "919437123456",
        upiId = "allinone.ragadi@okaxis",
        upiName = "ALL IN ONE STORE",
        maxDeliveryRadiusKm = 15.0,
        baseDeliveryFee = 20.0,
        baseDistanceKm = 3.0,
        perKmFee = 5.0,
        freeDeliveryMinAmount = 499.0,
        adminPin = "1234"
    )

    val defaultCustomer = CustomerProfileEntity(
        id = 1,
        name = "Biswajit Mohapatra",
        phone = "9438012345",
        address = "House No. 42, Ward 3, College Road",
        selectedArea = "Ragadi Bazar",
        landmark = "Opposite Shiv Temple",
        distanceKm = 1.2,
        isLoggedIn = true
    )

    val deliveryLocationPresets = listOf(
        DeliveryLocationPreset("Ragadi Bazar / College Road", 1.2, "Near Ragadi Panchayat Office", true),
        DeliveryLocationPreset("Jajpur Road Railway Station", 3.2, "Station Road, Main Market", true),
        DeliveryLocationPreset("Chorda Bypass Chowk", 4.1, "Near NH-215 Flyover", true),
        DeliveryLocationPreset("Vyasanagar Town Center", 4.8, "Near Municipality Office", true),
        DeliveryLocationPreset("Danagadi Chhak", 6.5, "Danagadi Market", true),
        DeliveryLocationPreset("Kalinganagar Tata Gate", 8.4, "Tata Steel Township Gate 1", true),
        DeliveryLocationPreset("Duburi Industrial Chowk", 11.2, "Duburi Bus Stand", true),
        DeliveryLocationPreset("Dhabaleswar Temple Area", 13.5, "Rural Bypass Link Road", true),
        DeliveryLocationPreset("Kuakhia Town (Beyond Service Area)", 22.0, "NH-16 Junction (Outside 15km)", false),
        DeliveryLocationPreset("Panikoili Toll Plaza (Beyond Service Area)", 28.5, "Toll Gate Area (Outside 15km)", false)
    )

    val initialCategories = listOf(
        CategoryEntity("cat_groceries", "Groceries & Staples", "shopping_bag", 1),
        CategoryEntity("cat_household", "Household & Cleaning", "cleaning_services", 2),
        CategoryEntity("cat_personal", "Personal Care", "spa", 3),
        CategoryEntity("cat_electronics", "Electronics & Accessories", "devices", 4),
        CategoryEntity("cat_clothing", "Clothing & Daily Wear", "checkroom", 5),
        CategoryEntity("cat_snacks", "Snacks & Beverages", "fastfood", 6)
    )

    val initialProducts = listOf(
        // Groceries & Staples
        ProductEntity(
            id = "prod_001",
            name = "Aashirvaad Shudh Chakki Atta",
            category = "Groceries & Staples",
            price = 210.0,
            mrp = 245.0,
            unit = "5 kg",
            stockQuantity = 45,
            description = "100% pure whole wheat flour processed with traditional chakki process for softest rotis.",
            iconType = "grocery",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_002",
            name = "Fortune Sunlite Refined Sunflower Oil",
            category = "Groceries & Staples",
            price = 135.0,
            mrp = 160.0,
            unit = "1 Litre Pouch",
            stockQuantity = 30,
            description = "Light and healthy cooking oil enriched with Vitamins A & D.",
            iconType = "grocery",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_003",
            name = "India Gate Basmati Rice Feast",
            category = "Groceries & Staples",
            price = 115.0,
            mrp = 140.0,
            unit = "1 kg",
            stockQuantity = 25,
            description = "Aromatic, aged long grain basmati rice perfect for biryani and daily rice dishes.",
            iconType = "grocery",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_004",
            name = "Tata Salt Vacuum Evaporated",
            category = "Groceries & Staples",
            price = 28.0,
            mrp = 30.0,
            unit = "1 kg",
            stockQuantity = 80,
            description = "Desh ka namak with essential iodine for health and mental development.",
            iconType = "grocery",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_005",
            name = "Fortune Kachi Ghani Mustard Oil",
            category = "Groceries & Staples",
            price = 150.0,
            mrp = 175.0,
            unit = "1 Litre Bottle",
            stockQuantity = 28,
            description = "Traditional cold pressed pure mustard oil with intense pungent flavour for authentic Odia curries.",
            iconType = "grocery",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_006",
            name = "Toor Dal (Arhar Dal) Premium",
            category = "Groceries & Staples",
            price = 160.0,
            mrp = 185.0,
            unit = "1 kg",
            stockQuantity = 35,
            description = "Unpolished protein rich toor dal, quick cooking and naturally aromatic.",
            iconType = "grocery",
            isFeatured = false
        ),

        // Household & Cleaning
        ProductEntity(
            id = "prod_007",
            name = "Surf Excel Easy Wash Detergent",
            category = "Household & Cleaning",
            price = 145.0,
            mrp = 165.0,
            unit = "1 kg",
            stockQuantity = 40,
            description = "Powerful stain removal powder that easily cleans tough stains with minimal scrub.",
            iconType = "household",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_008",
            name = "Vim Dishwash Gel with Lemon",
            category = "Household & Cleaning",
            price = 110.0,
            mrp = 125.0,
            unit = "500 ml Bottle",
            stockQuantity = 32,
            description = "Concentrated lime gel that degreases greasy vessels with just one spoon.",
            iconType = "household",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_009",
            name = "Harpic Power Plus Toilet Cleaner",
            category = "Household & Cleaning",
            price = 95.0,
            mrp = 105.0,
            unit = "500 ml",
            stockQuantity = 25,
            description = "10x better stain and limescale removal for sparkling clean, germ-free toilets.",
            iconType = "household",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_010",
            name = "Colin Glass & Surface Cleaner",
            category = "Household & Cleaning",
            price = 99.0,
            mrp = 115.0,
            unit = "500 ml Spray",
            stockQuantity = 18,
            description = "Streak-free shine spray for glass mirrors, car windows and TV screens.",
            iconType = "household",
            isFeatured = false
        ),

        // Personal Care
        ProductEntity(
            id = "prod_011",
            name = "Dettol Original Bathing Soap",
            category = "Personal Care",
            price = 120.0,
            mrp = 140.0,
            unit = "Pack of 4 x 75g",
            stockQuantity = 50,
            description = "Trusted germ protection soap with soothing pine fragrance for everyday family hygiene.",
            iconType = "care",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_012",
            name = "Head & Shoulders Cool Menthol",
            category = "Personal Care",
            price = 170.0,
            mrp = 195.0,
            unit = "180 ml",
            stockQuantity = 22,
            description = "Anti-dandruff cooling shampoo with revitalising menthol fragrance.",
            iconType = "care",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_013",
            name = "Colgate Strong Teeth Toothpaste",
            category = "Personal Care",
            price = 105.0,
            mrp = 120.0,
            unit = "200 g Saver Pack",
            stockQuantity = 60,
            description = "Amino Shakti formula with calcium boost for 2x stronger teeth.",
            iconType = "care",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_014",
            name = "Nivea Soft Light Moisturiser",
            category = "Personal Care",
            price = 160.0,
            mrp = 190.0,
            unit = "100 ml",
            stockQuantity = 15,
            description = "Fast absorbing non-greasy cream infused with Vitamin E and Jojoba oil.",
            iconType = "care",
            isFeatured = false
        ),

        // Electronics & Accessories
        ProductEntity(
            id = "prod_015",
            name = "boAt Type-C Fast Charging Cable",
            category = "Electronics & Accessories",
            price = 199.0,
            mrp = 399.0,
            unit = "1.2m Rugged Braided",
            stockQuantity = 20,
            description = "High-speed 3A fast charging and 480 Mbps data sync cable with durable braided cord.",
            iconType = "electronic",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_016",
            name = "Syska 9W Cool Daylight LED Bulb",
            category = "Electronics & Accessories",
            price = 160.0,
            mrp = 240.0,
            unit = "Pack of 2 (B22)",
            stockQuantity = 35,
            description = "Energy saving 9W LED bulb with bright 6500K daylight white illumination.",
            iconType = "electronic",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_017",
            name = "Ubon 2.4A Dual USB Mobile Charger",
            category = "Electronics & Accessories",
            price = 180.0,
            mrp = 299.0,
            unit = "1 Unit Adapter",
            stockQuantity = 16,
            description = "Compact wall charger with smart IC surge protection and dual USB outputs.",
            iconType = "electronic",
            isFeatured = false
        ),

        // Clothing & Daily Wear
        ProductEntity(
            id = "prod_018",
            name = "Cotton Men's Daily Wear T-Shirt",
            category = "Clothing & Daily Wear",
            price = 249.0,
            mrp = 399.0,
            unit = "Size L (Comfort Fit)",
            stockQuantity = 24,
            description = "100% breathable combed cotton round neck t-shirt ideal for Odisha summer comfort.",
            iconType = "clothing",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_019",
            name = "Pure Cotton Sambalpuri Odia Gamucha",
            category = "Clothing & Daily Wear",
            price = 89.0,
            mrp = 120.0,
            unit = "1 Piece (1.8m)",
            stockQuantity = 40,
            description = "Traditional handwoven Odia cotton gamucha towel with high water absorbency.",
            iconType = "clothing",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_020",
            name = "Cotton Large Bath Towel (70x140cm)",
            category = "Clothing & Daily Wear",
            price = 220.0,
            mrp = 350.0,
            unit = "1 Large Towel",
            stockQuantity = 15,
            description = "Super soft 450 GSM pure cotton terry towel with quick-dry technology.",
            iconType = "clothing",
            isFeatured = false
        ),

        // Snacks & Beverages
        ProductEntity(
            id = "prod_021",
            name = "Tata Tea Gold Rich Taste & Aroma",
            category = "Snacks & Beverages",
            price = 295.0,
            mrp = 330.0,
            unit = "500 g Pack",
            stockQuantity = 28,
            description = "Exquisite blend of Assam CTC teas with gently rolled long leaves for rich aroma.",
            iconType = "snack",
            isFeatured = true
        ),
        ProductEntity(
            id = "prod_022",
            name = "Haldiram's Nagpur Bhujia Sev",
            category = "Snacks & Beverages",
            price = 115.0,
            mrp = 130.0,
            unit = "400 g Pack",
            stockQuantity = 30,
            description = "Crunchy crispy moth flour and gram flour noodles tossed in authentic spices.",
            iconType = "snack",
            isFeatured = false
        ),
        ProductEntity(
            id = "prod_023",
            name = "Amul Taaza Homogenised Toned Milk",
            category = "Snacks & Beverages",
            price = 72.0,
            mrp = 75.0,
            unit = "1 Litre Tetra Pak",
            stockQuantity = 50,
            description = "UHT treated long life pure milk with zero preservatives and 3.0% milk fat.",
            iconType = "snack",
            isFeatured = false
        )
    )

    val sampleOrders = listOf(
        OrderEntity(
            id = "AIO-72910",
            customerName = "Biswajit Mohapatra",
            customerPhone = "9438012345",
            customerAddress = "House No. 42, Ward 3, College Road, Ragadi Bazar",
            landmark = "Opposite Shiv Temple",
            deliveryDistanceKm = 1.2,
            itemsSummary = "Aashirvaad Atta (5kg) x 1, Surf Excel (1kg) x 1",
            itemsJson = """[{"name":"Aashirvaad Shudh Chakki Atta","unit":"5 kg","price":210.0,"quantity":1},{"name":"Surf Excel Easy Wash Detergent","unit":"1 kg","price":145.0,"quantity":1}]""",
            subtotal = 355.0,
            deliveryFee = 20.0,
            totalAmount = 375.0,
            paymentMethod = "COD",
            paymentStatus = "Pending",
            upiTransactionRef = "",
            orderStatus = "Out for Delivery",
            orderTime = System.currentTimeMillis() - 25 * 60 * 1000,
            statusNote = "Delivery partner Rajesh is on his way to Ragadi College Road"
        ),
        OrderEntity(
            id = "AIO-72884",
            customerName = "Priyanka Rout",
            customerPhone = "9438098765",
            customerAddress = "Flat 204, Gayatri Enclave, Station Road",
            landmark = "Near Jajpur Road Railway Station",
            deliveryDistanceKm = 3.2,
            itemsSummary = "Fortune Sunflower Oil x 2, Tata Tea Gold x 1",
            itemsJson = """[{"name":"Fortune Sunlite Refined Sunflower Oil","unit":"1 Litre Pouch","price":135.0,"quantity":2},{"name":"Tata Tea Gold Rich Taste & Aroma","unit":"500 g Pack","price":295.0,"quantity":1}]""",
            subtotal = 565.0,
            deliveryFee = 0.0, // Free delivery for > 499
            totalAmount = 565.0,
            paymentMethod = "UPI",
            paymentStatus = "Verified",
            upiTransactionRef = "UPI-REF-429910294102",
            orderStatus = "Preparing",
            orderTime = System.currentTimeMillis() - 45 * 60 * 1000,
            statusNote = "Items being packed at Ragadi store"
        ),
        OrderEntity(
            id = "AIO-71402",
            customerName = "Debashis Jena",
            customerPhone = "9438012345",
            customerAddress = "Plot 12, Danagadi Chhak",
            landmark = "Near State Bank ATM",
            deliveryDistanceKm = 6.5,
            itemsSummary = "boAt Type-C Cable x 1, Syska LED Bulb x 1",
            itemsJson = """[{"name":"boAt Type-C Fast Charging Cable","unit":"1.2m Rugged Braided","price":199.0,"quantity":1},{"name":"Syska 9W Cool Daylight LED Bulb","unit":"Pack of 2 (B22)","price":160.0,"quantity":1}]""",
            subtotal = 359.0,
            deliveryFee = 38.0,
            totalAmount = 397.0,
            paymentMethod = "COD",
            paymentStatus = "Paid on Delivery",
            upiTransactionRef = "",
            orderStatus = "Delivered",
            orderTime = System.currentTimeMillis() - 24 * 3600 * 1000,
            statusNote = "Delivered successfully yesterday"
        )
    )
}
