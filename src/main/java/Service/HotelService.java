package Service;

import Dto.Request.HotelRequest;
import Dto.Request.SearchRequest;
import Dto.Response.BookHotelResponse;
import Dto.Response.HotelCustomerGetResponse;
import Dto.Response.HotelResponse;
import Entity.AddressHotel;
import Entity.Hotel;
import Entity.UserEntity;
import Exception1.ResourceNotFoundException;
import MapperData.HotelMapper;
import Repository.HotelRepository;
import Repository.Specification.HotelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private AddressHotelService addressHotelService;

    public HotelResponse add(HotelRequest hotelRequest) {
        Hotel hotel = hotelMapper.toHotel(hotelRequest);

        UserEntity userCreated = userService.addUserHotel(hotel.getUser());
        AddressHotel addressHotelCreated = addressHotelService.add(hotel.getAddressHotel());
        hotel.setAddressHotel(addressHotelCreated);

        hotel.setUser(userCreated);
        return hotelMapper.toHotelResponse(hotelRepository.save(hotel));
    }

    public List<HotelCustomerGetResponse> getAll(){
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream().map(hotelMapper::toHotelCustomerGetResponse).collect(Collectors.toList());
    }

    public Hotel getHotelById(Long idHotel) {
        return hotelRepository.findById(idHotel).get();
    }

    public HotelResponse detail(Long id){
        return hotelMapper.toHotelResponse(hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("hotel not found")));
    }

    public Hotel getHotelByUserId(Long idUser) {
        return hotelRepository.findByUser_Id(idUser);
    }

    public List<HotelResponse> search(SearchRequest searchRequest) {
        Specification<Hotel> spec = Specification.where(null); // bắt đầu với điều kiện rỗng

        if (searchRequest.getNameHotel() != null && !searchRequest.getNameHotel().isEmpty()) {
            spec = spec.and(HotelSpecification.hasHotelName(searchRequest.getNameHotel()));
        }

        if (searchRequest.getProvince() != null && !searchRequest.getProvince().isEmpty()) {
            spec = spec.and(HotelSpecification.hasProvince(searchRequest.getProvince()));
        }

        List<Hotel> hotels = hotelRepository.findAll(spec);

        return hotels.stream()
                .map(hotel -> hotelMapper.toHotelResponse(hotel))
                .collect(Collectors.toList());
    }

}
