package Service;

import Dto.Request.CustomerRequest;
import Dto.Response.CustomerResponse;
import Entity.Customer;
import Entity.UserEntity;
import MapperData.CustomerMapper;
import Repository.CustomerRepository;
import Repository.UserRepository;
import Util.ValidaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ValidaResponse validaResponse;

    public CustomerResponse add(CustomerRequest customerRequest) {
        Customer customer = customerMapper.toCustomer(customerRequest);
        UserEntity userCreated = userService.addUserCustomer(customer.getUser());
        customer.setUser(userCreated);
        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse me(Long idUser){
        validaResponse.checkUserIdMathToken(idUser);
        Customer customer = customerRepository.findByUserId(idUser);
        return customerMapper.toCustomerResponse(customer);
    }

    public Customer getCustomerByIdUser(Long idUser) {
        return customerRepository.findByUserId(idUser);
    }
}
