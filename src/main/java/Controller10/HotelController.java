package Controller10;

import Dto.Request.HotelRequest;
import Dto.Request.SearchRequest;
import Dto.Response.BookHotelResponse;
import Dto.Response.HotelCustomerGetResponse;
import Dto.Response.HotelResponse;
import Service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping()
    public HotelResponse add(@RequestBody HotelRequest hotelRequest) {
        return hotelService.add(hotelRequest);
    }

    @GetMapping()
    public List<HotelCustomerGetResponse> get() {
        return hotelService.getAll();
    }


    @PostMapping("/search")
    public List<HotelResponse> search(@RequestBody SearchRequest searchRequest) {
        return hotelService.search(searchRequest);
    }

    @GetMapping("/detail")
    public HotelResponse detail(@RequestParam(name = "id") Long id){
        return hotelService.detail(id);
    }


    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        try {
            String apiKey = "4ddede4ada64aae1e0793f5c61eba4b4";
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.imgbb.com/1/upload?key=" + apiKey))
                    .POST(HttpRequest.BodyPublishers.ofString("image=" + URLEncoder.encode(base64, StandardCharsets.UTF_8)))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            String imageUrl = json.getJSONObject("data").getString("url");
            result.put("img", imageUrl);
            return ResponseEntity.ok(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result.put("error",e.getMessage());
            return ResponseEntity.ok(result);
        }
    }


}
