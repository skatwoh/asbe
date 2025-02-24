#Biểu đồ ER (Entity-Relationship):

Hiển thị mối quan hệ giữa các bảng trong cơ sở dữ liệu
Thể hiện rõ các mối quan hệ 1-1, 1-nhiều, nhiều-nhiều
Dễ dàng nhìn thấy cách các thực thể kết nối với nhau
```mermaid
erDiagram
    %% Bảng người dùng và các tương tác chính
    Users ||--o{ Orders : "đặt"
    Users ||--o{ Bids : "đấu giá"
    Users ||--o{ Cart : "sở hữu"
    Users ||--o{ Reviews : "viết"
    Users ||--o{ AuctionWinners : "thắng"

    %% Bảng sách và quan hệ với các thực thể khác
    Books ||--o{ OrderItems : "thuộc"
    Books ||--o{ Cart : "có trong"
    Books ||--o{ Reviews : "nhận"
    Books ||--o{ AuctionItems : "được đấu giá"
    Books }|--|| Publishers : "được xuất bản bởi"

    %% Quan hệ nhiều-nhiều giữa Books với Authors và Categories
    Books }o--o{ Authors : "được viết bởi"
    Books }o--o{ Categories : "thuộc về"

    BookAuthors }|--|| Books : "gồm"
    BookAuthors }|--|| Authors : "được viết bởi"

    BookCategories }|--|| Books : "gồm"
    BookCategories }|--|| Categories : "trong"

    %% Danh mục sách và quan hệ phân cấp
    Categories ||--o{ Categories : "có danh mục con"

    %% Đơn hàng và sản phẩm trong đơn
    Orders ||--o{ OrderItems : "chứa"

    %% Đấu giá
    AuctionSessions ||--o{ AuctionItems : "chứa"
    AuctionItems ||--o{ Bids : "nhận giá thầu"
    AuctionItems ||--o| AuctionWinners : "được thắng bởi"

```

Biểu đồ Luồng Đấu Giá:

Mô tả chi tiết quy trình đấu giá từ đầu đến cuối
Hiển thị các bước: tạo phiên, kích hoạt item, đấu giá, chuyển item tiếp theo
Bao gồm xử lý người thắng và thanh toán
```mermaid
flowchart TD
    subgraph Phiên Đấu Giá
        Start([Bắt đầu]) --> CreateSession[Tạo phiên đấu giá]
        CreateSession --> AddItems[Thêm 4 items vào đấu giá]
        AddItems --> SessionStart{Đã đến giờ bắt đầu?}

        SessionStart -- "Chưa" --> Wait[Chờ đến 2h chiều]
        Wait --> SessionStart

        SessionStart -- "Đúng giờ" --> ActivateSession[Kích hoạt phiên đấu giá]
        ActivateSession --> ActivateFirstItem[Kích hoạt item đầu tiên]
    end

    subgraph Vòng Đấu Giá
        ActivateFirstItem --> UserBidding[Người dùng đấu giá]
        UserBidding --> ValidBid{Giá hợp lệ?}

        ValidBid -- "Không" --> RejectBid[Từ chối lượt đấu]
        RejectBid --> UserBidding

        ValidBid -- "Có" --> RecordBid[Ghi nhận giá thầu]
        RecordBid --> UpdatePrice[Cập nhật giá hiện tại]
        UpdatePrice --> ItemEnded{Item đã kết thúc?}

        ItemEnded -- "Chưa" --> UserBidding
        ItemEnded -- "Đã kết thúc" --> DetermineWinner[Xác định người thắng]
        DetermineWinner --> RecordWinner[Ghi nhận người thắng]
    end

    subgraph Hoàn Tất
        RecordWinner --> NextItem{Còn item nữa không?}

        NextItem -- "Có" --> ActivateNextItem[Kích hoạt item tiếp theo]
        ActivateNextItem --> UserBidding

        NextItem -- "Không" --> CompleteSession[Kết thúc phiên đấu giá]
        CompleteSession --> PaymentProcess[Xử lý thanh toán]
        PaymentProcess --> Delivery[Giao hàng]
        Delivery --> End([Hoàn thành])
    end
```

Biểu đồ Luồng Đặt Hàng Thông Thường:

Mô tả quy trình mua sách thông thường (không đấu giá)
Từ duyệt sách, thêm vào giỏ, thanh toán đến giao hàng
Thể hiện đầy đủ các bước xử lý đơn hàng
```mermaid
flowchart TD
    Start([Bắt đầu]) --> Browse[Duyệt sách]
    Browse --> AddToCart[Thêm vào giỏ hàng]
    AddToCart --> Cart[Xem giỏ hàng]
    
    Cart --> UpdateCart[Cập nhật số lượng]
    UpdateCart --> Cart
    
    Cart --> Checkout[Thanh toán]
    Checkout --> ShippingInfo[Nhập thông tin giao hàng]
    ShippingInfo --> PaymentMethod[Chọn phương thức thanh toán]
    
    PaymentMethod --> ValidPayment{Thanh toán thành công?}
    ValidPayment -->|Không| RetryPayment[Thử lại]
    RetryPayment --> PaymentMethod
    
    ValidPayment -->|Có| CreateOrder[Tạo đơn hàng]
    CreateOrder --> UpdateInventory[Cập nhật kho]
    UpdateInventory --> OrderConfirmation[Xác nhận đơn hàng]
    OrderConfirmation --> OrderFulfillment[Xử lý đơn hàng]
    OrderFulfillment --> Shipping[Giao hàng]
    Shipping --> OrderComplete[Hoàn thành đơn hàng]
    OrderComplete --> End([Kết thúc])
```

Biểu đồ Luồng Dữ Liệu:

Phân nhóm các chức năng chính của hệ thống
Hiển thị cách dữ liệu di chuyển giữa các thành phần
Mô tả các quy trình nghiệp vụ chính
```mermaid
flowchart LR
    subgraph "Quản lý Sách"
        BookManagement[Quản lý Sách] --> Books[(Books)]
        BookManagement --> Authors[(Authors)]
        BookManagement --> Publishers[(Publishers)]
        BookManagement --> Categories[(Categories)]
        Books --- BookAuthors[(BookAuthors)]
        Books --- BookCategories[(BookCategories)]
        BookAuthors --- Authors
        BookCategories --- Categories
    end
    
    subgraph "Mua Sách Thường"
        UserBrowse[Người dùng duyệt sách] --> Books
        UserBrowse --> Cart[(Cart)]
        Cart --> CheckoutProcess[Quy trình thanh toán]
        CheckoutProcess --> Orders[(Orders)]
        CheckoutProcess --> OrderItems[(OrderItems)]
        Orders --> OrderFulfillment[Thực hiện đơn hàng]
        OrderFulfillment --> OrderStatusUpdate[Cập nhật trạng thái]
        OrderStatusUpdate --> Orders
    end
    
    subgraph "Đấu Giá Sách"
        AuctionSetup[Thiết lập đấu giá] --> AuctionSessions[(AuctionSessions)]
        AuctionSetup --> AuctionItems[(AuctionItems)]
        AuctionItems --> Books
        
        UserBid[Người dùng đấu giá] --> AuctionItems
        UserBid --> Bids[(Bids)]
        
        AuctionEnd[Kết thúc đấu giá] --> AuctionItems
        AuctionEnd --> AuctionWinners[(AuctionWinners)]
        AuctionEnd --> NextItemTrigger[Trigger kích hoạt item tiếp]
        NextItemTrigger --> AuctionItems
        
        AuctionWinners --> WinnerPayment[Xử lý thanh toán]
        WinnerPayment --> AuctionWinners
    end
    
    Users[(Users)] --> UserBrowse
    Users --> UserBid
    
    subgraph "Đánh Giá"
        OrderComplete[Đơn hàng hoàn thành] --> ReviewPrompt[Gợi ý đánh giá]
        ReviewPrompt --> Reviews[(Reviews)]
        Reviews --> Books
    end
```
