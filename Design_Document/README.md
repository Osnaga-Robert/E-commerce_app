# E-commerce app - Osnaga Robert Viorel

## Introduction

Currently, e-commerce applications have become essential for businesses as well as for consumers. These applications are online platforms that allow sellers to showcase their products to consumers in a way that can be viewed with a click, from any device.

The utility of e-commerce applications:
- **Accessibility** - The application is available non-stop, allowing buyers to view and purchase products at any time.
- **Extended market** - This application has no coverage limits; it can be accessed from anywhere, so products have a global reach.
- **Cost efficiency** - An e-commerce application is cheaper to maintain than a physical store.

We aim to develop an e-commerce application through which a seller can easily showcase their products, observe statistics on sales and products, and where the buyer has an easy-to-use interface to find the desired product.

## Technologies used:

- **Front-end** - Angular
- **Back-end** - Java
- **Database** - MySQL

## Application functionalities

When we first enter the site, we will be able to view the products already added without having permission to buy or become a seller. For these features, we need to create an account. There will be two types: a buyer's account that can be created immediately and a seller's account which, after completing the mandatory fields, will have to wait for an acceptance or rejection response because the entered data needs to be verified.

Thus, we will have four actors:

- **Visitor** - the actor who can only view the products
- **Buyer** - the actor who can view and purchase products
- **Seller** - the actor who lists their products for sale
- **Admin** - the actor who manages issues

Visitor:

- Viewing products: The visitor can explore and see details about various products available on the site.
- Searching for specific products: The user can use the search bar to find specific products by name.
- Selecting a list of products by category: The visitor can navigate through different categories to find the desired products.
- Applying filters to a category: The user can filter products in categories by criteria such as price, brand, rating, etc.
- Requesting help from an administrator: The visitor can request assistance or additional information from a site administrator.
- Creating an account or registering: The visitor has the option to create an account to benefit from additional functionalities.
- Viewing reviews: The visitor can read reviews and ratings from other users to learn more about products of interest.
- Access to offers and promotions: Visitors can see current offers and promotions available on the site.

Buyer (in addition to visitor functionalities):

- Adding products to the cart: The buyer can add products to the shopping cart for later purchase.
- Completing orders: The user can complete the purchasing process by placing an order.
- Online payment: The buyer can make payments for ordered products using various online payment methods.
- Order tracking: The user can check the delivery status for placed orders.
- Viewing order history: The buyer can see a list of all previous orders.
- Writing reviews: The user can write and publish reviews for purchased products.
- Email notifications: The buyer can receive notifications and updates via email about their orders and other relevant activities.
- Saving products to the wish list: The visitor can save preferred products to a wish list for review or purchase later.

Seller:

- Inventory management: The seller can monitor and update available stock.
- Adding a product: The seller can add new products to the eCommerce platform.
- Modifying product characteristics: The seller can edit the details and specifications of existing products.
- Applying discounts: The seller can set discounts and promotions for certain products.
- Sales analysis: The seller can access reports and statistics about sales performance.
- Managing orders and deliveries: The seller can track and manage received orders and delivery processes.
- Accessing technical support: The seller can request technical help for issues encountered on the platform.
- Managing feedback: The seller can view and respond to reviews and feedback left by customers.

Administrator:

- Resolving issues reported by buyers, sellers, and visitors: The administrator manages and solves any problems reported by site users.
- Verifying registration requests for buyer accounts: The administrator validates and approves registration requests from new buyers.
- Monitoring performance: The administrator oversees the performance of the site and its users.
- Adding functionalities: The administrator can implement new features and improvements on the platform.
- Managing site content: The administrator can edit and update content on product pages, informational pages, and other sections of the site.
- Site security: The administrator ensures the site is protected against security threats and manages user permissions.

## User stories

### USER STORY #1 - Searching for products

As a **visitor** or **buyer** 

**I want to** search for a specific product by name

**To** find the desired product.

**Acceptance criteria:**

**AC#1 - Finding desired products**

In the **search bar**, type the 

product and press **enter**, 

and the found products are displayed.

**AC#2 - The desired product does not exist**

In the **search bar**, type the 

desired product and press **enter**, 

and an empty page with an appropriate 

message is displayed.

### USER STORY #2 - Selecting a product category

As a **visitor** or **buyer** 

**I want to** search for a specific product by category

**To** find products from the desired category.

**Acceptance criteria:**

**AC#1 - Selecting the desired category**

On the left side of the **main page**, 

there is a **column** with all available 

**categories** from which the **user** can choose.

### USER STORY #3 - Registering as a buyer

As a **visitor**

**I want to create** an account

**To** buy products.

**Acceptance criteria:**

**AC#1 - Accessing the registration page**

As a **visitor**, access the **login** page and click on the **Register** button.

**AC#2 - Registration form**

After step **AC#1**, fill in the form below with the following **data**:

| **Field Name** | **Field Type** | **Mandatory/Optional** |
| -------------- | -------------- | ---------------------- |
| Name           | Free text <br> Min 6 characters <br> Max 20 characters | Mandatory |
| First Name     | Free text <br> Min 6 characters <br> Max 20 characters | Mandatory |
| Email          | Free text <br> Min 6 characters <br> Max 20 characters | Mandatory |  
| Password       | Free text <br> Min 6 characters <br> Max 20 characters | Mandatory |
| Repeat Password| Free text <br> Min 6 characters <br> Max 20 characters | Mandatory |

**Errors that may occur**

**Error 1 - Incorrect email format**

The **correct** form of an 

**email** should be in the form 

**firstname@example.com**

**Error 2 - Not meeting minimum or maximum values**

**Completed fields** must contain 

**at least** 6 characters and 

**at most** 20 characters

**Error 3 - Password and Repeat Password contain different passwords**

The **two fields** must contain the **same password**

**Error 4 - Email already used**

The **email** entered is already 

in use, so an account with that email **already exists**

### USER STORY #4 - Adding products to the cart

As a **buyer**

**I want to** add desired products

**To** the shopping cart.

**Acceptance criteria:**

**AC#1 - Adding a product to the cart**

**The buyer** selects the product, accesses 

its **page**, and clicks on the **Add to cart** button.

### USER STORY #5 - Writing a review

As a **buyer**

**I want to** write a review

about a **purchased** product.

**AC#1 - Writing a review**

**The buyer** selects the product's **page** 

they purchased, **gives a rating** to the product, 

and additionally **completes** a description.

**Errors that may occur**

**Error 1 - Incorrect product page**

**The buyer** can access the page of

**another product** they did not purchase, 

not having access to write a **review**.

### USER STORY #6 - Registering as a seller

As a **visitor**

**I want to create** an account

**To** sell my products.

**Acceptance criteria:**

**AC#1 - Completing data**

**The visitor**, after accessing the 

**registration page** from the **main page**, 

completes the necessary data, after which they 

will receive an email with the account acceptance or not.

### USER STORY #7 - Adding products

**The seller** adds one or more products.

**Acceptance criteria:**

**AC#1 - Adding a single product**

**The seller** adds a single product 

 completing the following **data**:

| **Field Name**  | **Field Type** |
| --------------- | -------------- |
| Product Name    | Free text |
| Product Category| Free text |
| Description     | Free text |
| Specifications  | Free text |

**AC#2 - Adding multiple products**

**The seller** can add multiple products using a **csv file**.

### USER STORY #8 - Applying discounts

**The buyer** wants to **add** a **discount** to one or more products.

**Acceptance criteria**

**AC#1 - Selecting products**

**The seller** goes to the discount page,

**selects** from their product list the ones 

that will have a discount, and enters the **percentage**.

### USER STORY #9 - Approving accounts

As an **administrator**, we want to approve 

some requests from **sellers** to sell.

**Acceptance criteria**

**AC#1 - Verifying accounts**

**The administrator** accesses the 

**newly registered accounts** page and 

verifies their correctness. If **everything** 

is ok, they approve the **account**, 

if not, they **deny** it.

**AC#2 - Denial reasons**

**The administrator** can **add reasons** 

for which the **request** was **rejected**.

**AC#2 - Approving accounts**

**The administrator**, after the step in

**AC#1**, decides if the seller is eligible

to sell on the site; otherwise, they will refuse the request

by clicking the **decline** button.

## Communication Protocol

### For client-server connection, we will use **HTTPS**

### For sending emails, we will use **SMTP**

## Message Flow

### Visitor accesses the web page

**GET** request that returns the main page **e-commerce.html**

### Viewing a product

**The visitor** selects a product, a

**GET /product/{product_id}** request is sent, and **the visitor**

receives the product page.

### Log in

**The user** enters their email and password,

**the browser** sends a request of type **POST /api/auth/login**,

with the following body **{ "username": "user@example.com", "password": "password123" }**

### Adding a product to the cart

**The buyer** adds a product to the cart,

**the browser** sends a request of type

**POST /cart/add** with **{ "productId": "{productID}", "quantity": 1 }**

### Completing an order

**The buyer** completes the checkout process,

**the browser** sends a request of type

**POST /order/checkout** with 

**{ "userId": "67890", "cart": [...], "paymentMethod": "creditCard", "shippingAddress": {...} }**

### Writing and submitting a review

**The visitor** writes a review for a purchased product,

**the browser** sends a request of type

**POST /review/submit** with

**{ "productId": "12345", "userId": "67890", "rating": 5, "comment": "Recomand!" }**

### Adding a product by the seller

**The seller** goes to the add product page,

fills in the **details**, and **the browser** sends

a request of type **POST /api/addProduct** with

**{"sellerId": "67890","productName": "Produs Nou","description": "Descriere detaliata a produsului","price": 100.0,"category": "Electronics","stock": 1500}**

### Applying discounts

**The seller** goes to the discounts page, selects

**the products** to which a **discount** is applied, and

**the browser** sends a request of type

**POST /discount/apply** with

**{"productIds": [1,2,3,4],"sellerId": "67890","discountPercentage": 20,"duration": 7}**

### Approving accounts

**The administrator** goes to the pending accounts page,

checks the details submitted by the seller, and approves or rejects the request,

**the browser** sends a request of type

**POST /account/approve** with

**{"accountId": "12345","approvalStatus": true}**

or

**{"accountId": "12345","approvalStatus": false}**

## Scalability Considerations

### Machine specifications

**CPU** : 4-8 processors

**RAM** : 16-32 GB RAM

**Storage** : 250GB SSD

**Network** : high-speed internet connection, 1 Gbps

### Maximum load

**RPS** - between a minimum of 300 and a maximum of 700

**Simultaneous users** - 5000 simultaneous users

**Number of simultaneous transactions** - minimum 1000, maximum 3000

## Application Diagram

![App_Diagram](../Images/Design_Document/App_Diagram.png)

## UML Diagrams

### Create Catalog

![Categories](../Images/Design_Document/Singleton.png)

### Notifying subscribed buyers

![News](../Images/Design_Document/Observer.png)

### Create Account

![Register](../Images/Design_Document/Factory.png)



