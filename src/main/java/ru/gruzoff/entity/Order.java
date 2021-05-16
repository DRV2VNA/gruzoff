package ru.gruzoff.entity;

import java.util.List;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "customerId")
    Customers customerId;

    @OneToOne
    @JoinColumn(name = "driverId")
    Drivers driverId;

    @OneToOne
    @JoinColumn(name = "carId")
    Car carId;

    float price;

    protected String status;

    @ManyToOne
    @JoinColumn(name = "order_details_id", referencedColumnName = "id")
    protected OrderDetails orderDetails;

    @ManyToMany
    @JoinColumn(name = "loaders_id", referencedColumnName = "id")
    protected List<Loaders> loaders;

    @ManyToMany
    @JoinColumn(name = "extra_people_on_order_id", referencedColumnName = "id")
    protected List<Customers> extraCustomers;

    protected boolean sended;


}
