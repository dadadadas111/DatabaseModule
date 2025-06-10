package example.mongodb;

import impl.mongodb.MongoDbProvider;
import impl.mongodb.MongoDbAdapter;
import example.models.Customer;
import java.util.List;

public class MongoDbConsoleDemo {
    public static void main(String[] args) {
        String uri = "YOUR_DB_URI";
        MongoDbProvider provider = new MongoDbProvider();
        provider.connect(uri);
        MongoDbAdapter<Customer> customerAdapter = provider.getAdapter(Customer.class);

        String testId = "c001";
        // Xóa trước nếu đã tồn tại
        Customer existed = customerAdapter.findById(testId);
        if (existed != null) {
            customerAdapter.delete(existed);
            System.out.println("Deleted old customer with id: " + testId);
        }

        // Thêm mới một khách hàng
        Customer newCustomer = new Customer(testId, "Nguyen Van A");
        customerAdapter.insert(newCustomer);
        System.out.println("Inserted customer: " + newCustomer.id);

        // Lấy tất cả khách hàng
        List<Customer> customers = customerAdapter.findAll();
        System.out.println("All customers:");
        for (Customer c : customers) {
            System.out.println(c.id + " - " + c.name);
        }

        // Tìm theo ID
        Customer found = customerAdapter.findById(testId);
        if (found != null) {
            System.out.println("Found customer: " + found.id + " - " + found.name);
            // Cập nhật
            found.name = "Nguyen Van B";
            customerAdapter.update(found);
            System.out.println("Updated customer: " + found.id + " - " + found.name);

            // Xóa
//            customerAdapter.delete(found);
            System.out.println("Deleted customer: " + found.id);
        } else {
            System.out.println("Customer not found for update/delete.");
        }
    }
}
