# Order postgresql ms example

Start service with database

> docker compose -f .\docker-compose.yml up

Retrieve Order endpoint
```
curl --location --request GET 'localhost:8080/papel/orders/RO-XYZ-0129'
```

Create Order endpoint
```
curl --location --request POST 'localhost:8080/papel/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "order_number" : "RO-XYZ-0129",
    "order_items":  [
        {
            "product_name":  "Printer",
            "quantity": 1,
            "price": 350.50
        },
		{
            "product_name":  "Ink set",
            "quantity": 1,
            "price": 39.99
        }
    ]
}'
```

Change Order status endpoint
```
curl --location --request PATCH 'localhost:8080/papel/orders/status/RO-XYZ-0129' \
--header 'Content-Type: application/json' \
--data-raw '{
  "status": "DELIVERED",
  "version": 0
}'
```