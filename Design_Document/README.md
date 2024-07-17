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



## Prototype

### Main page

![Main_page](../Images/Design_Document/Main%20Page%20-%20Desktop.png)

Main page of the buyer and visitor accounts, where you can see the categories that the website are sellings and some of the best sellers

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- Category buttons - press to go to a category of products
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opnes the log in page
- Image, Product Name, Price - type on one of those to open a product's page

### Product page

![Product_page](../Images/Design_Document/Product%20-%20link.png)

The product page contains the images of the product, the price and the description about his functionalities. Reviews are written by the persons who bought this item

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- description button - press to view the product's description
- review button - press to view reviews from buyers
- add to cart button - press to add the product to cart if you are logged in, otherwise it opens the log in page

### Cart

![Cart](../Images/Design_Document/Cart.png)

The cart is the page where all the products that a buyer added to cart can see and place the order

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- Quantity button - show your quantity and ce be modified
- Delete from cart - Delte the product from the cart
- Image button - Press to go to the product's page
- Place the order button - used to go to the order details

### Order details

![Order_details](../Images/Design_Document/Delivery%20Info.png)

Order details page consists of filling in some billing and deliveery data

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- Select county - select a county from a list
- Type your city - The city where your order should come
- Type street - The street where your order should come
- Type address - The address your order should come
- Type phone - The phone number for the delivery team
- Comple the order button - Button to finish the order

### Log in

![Log_in](../Images/Design_Document/Log%20In.png)

The log in page where the user need to complete the email address and password to be a buyer ir a seller based on his type of account

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- Type your email - The email used to register on the website
- Type your password - The password used to register on the website
- Connect button - used to connect to the account, if it correct it will goo to the main page, if not an error message will appear

### Register buyers

![Register_buyers](../Images/Design_Document/Register%20-%20Buyer.png)

A page where the viewer need to complete some datas to be a buyer

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- Type your First Name - client's first name
- Type your Last Name - client's last name
- Type your email - client's email to log in to the website and receive mails
- Type your password - client's password to log in to the website
- Type your password - as above, the two passwords must be identical
- Create account button - button to confirm your data, if everything is alright the client is redirected to the main page, otherwise something is wrong and an error message will appear

### Register seller

![Register_seller](../Images/Design_Document/Register%20-%20Seller.png)

A page where the viewer need to complete some datas to be a seller. Also the seller nee to wait some time to be accepted.

- Search bar - you can type for a product
- SHOP4ALL - press to come back to main page
- the favorites button, leads you to the favorites page if you are logged in, otherwise it opens the log in page
- cart button - press to go a the cart page if you are logged in, otherwise it opens the log in page
- account button - press to see information about the account if you are looged in, otherwise it opens the log in page
- Type your first name - seller's first name
- Type your last name - seller's last name
- Type Comapany Name - seller's company name
- Type your email - seller's email to log in
- Type your email - seller's email to log in to the website and receive mails
- Type your password - seller's password to log in to the website
- Type your password - as above, the two passwords must be identical
- Type a short description about the products - the seller must give us some examples of the products to see if they fit the requirements of our site
- Create account button - button to confirm your data, if everything is alright the seller is redirected to the main page, otherwise something is wrong and an error message will appear

### Dashboard

![Dashboard](../Images/Design_Document/Dashboard.png)

The main page of the seller, where he can see some statistics
about his total sales and sales on a week and total customers of all time and of a week.

Also, he can see the monthly sales on a chart and best selling products

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account

### Add product

![Add_products](../Images/Design_Document/Add%20product.png)

Add products page where the seller can add a new product based on the left panel from the left or he can add multiple products using a csv file based on the information from the right panel.

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- Type your product name - the new product's name
- Select category - select a category in which the product falls
- Select subcategory - select a subcategory in which the product falls
- Add Price - price for the product
- Add Stock - the number of products you can sell
- Add a description about your product - The description that will see the buyers
- Add products button - add the product
- Upload csv file - a button to upload a csv file based on the information bellow
- Add products button - add the products from the csv file

### Reports

![Reports](../Images/Design_Document/Reports.png)

The chart page displays information about products and customers to the seller in order to better manage the customers and products they want to continue with.

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- Per week buttons - We can choose for the charts to be per days, weeks, months or years

### Orders

![Orders](../Images/Design_Document/Orders.png)

The order page contains all the orders made by clients at a specific seller. The seller need to confirm that the order is on delivery, he also can speak with the client and drop the order if something happened.

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- Confirm the order button - a button that will send an email to the buyer about the delivery of the command
- Speak with the client button - seller can speak with the buyer about informations about his order details
- Drop the order button - seller can reject orders

### Discounts

![Discounts](../Images/Design_Document/Discounts.png)

The purpose of the discount page is to add a new offer of a product or an entire category. The seller can also extend the campaign or remove it.

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- < Select from here > - choose a product or a category
- ___ % - complete with the discount percentage
- From ____ To ____ - complete the start and the finish of the discount
- Add to list - button to add the discount to the right panel
- Apply discounts - button to confirm the discounts
- Extend the offer - button to extend the discount of a product/category
- Delete discount - finish the discount time earlier

### Reviews

The purpose of the review page is to display feedback from customers, how satisfied they are with the product. The seller can respond to these reviews or can remove this review notice from his page

![Reviews](../Images/Design_Document/Reviews.png)

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- Comment button - seller can speak with the buyer
- mark as seen button - seller can delete the message from the review page

### Products

![Products](../Images/Design_Document/Products.png)


The product page shows the seller all the products he has for sale as well as the description, price and stock. He can modify all the mentioned or remove the product if it is no longer in stock or it no longer makes sense to continue with it.

- SHOP4ALL - button to go back to dashboard
- Add product button - go to add product page
- Reports button - go to reports page
- Discounts button - go to discounts page
- Reviews button - go to reviews page
- Products button - go to products page
- Log out - log out from the seller account
- Add stock button - the seller can introduce more stock to a products
- Change characteristics - the seller can change the name, the description of the product or the price
- Delete the products - the seller can delete the product