package backend.util;

import backend.model.Restaurant;
import backend.model.User;
import backend.repository.RestaurantRepository;
import backend.repository.UserRepository;
import backend.security.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class GetterUtil {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final JwtUtil jwtUtil;

    public GetterUtil(UserRepository userRepository, RestaurantRepository restaurantRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.jwtUtil = jwtUtil;
    }


    // Getting the id of a restaurant by it's manager id
    public Integer getRestaurantId(String managerUsername){
        Optional<User> managerOptional = userRepository.findByUsernameAndIsDeleted(managerUsername, false);
        if (managerOptional.isPresent()){
            User manager = managerOptional.get();
            Optional<Restaurant> restaurantOptional = restaurantRepository.findByManagerId(manager.getId());
            if (restaurantOptional.isPresent()){
                Restaurant restaurant = restaurantOptional.get();
                return restaurant.getId();
            }else {
                throw new NullPointerException("Restaurant not found");
            }
        }else {
            throw new NullPointerException("Manager not found");
        }
    }

    // Getting the username from the token in request
    public String getUsernameFromRequest(HttpServletRequest request){
        final String requestTokenHeader = request.getHeader("Authorization");
        // Removing Bearer from token
        String jwtToken = requestTokenHeader.substring(7);
        return jwtUtil.getUsernameFromToken(jwtToken);
    }
}
