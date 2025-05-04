package Util;


import Entity.Customer;
import Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ValidaResponse {
    @Autowired
    private CustomerRepository customerRepository;
    public void checkUserIdMathToken(Long idUser){
        Customer customer = customerRepository.findByUserId(idUser);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if(!customer.getUser().getEmail().equals(email)){
            throw new BadCredentialsException("Account not match token!");
        }
    }
}
