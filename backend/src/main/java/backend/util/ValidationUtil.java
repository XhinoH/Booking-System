package backend.util;

import backend.model.Menu;
import backend.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ValidationUtil {

    private final MenuRepository menuRepository;
    private final GetterUtil getterUtil;

    public ValidationUtil(MenuRepository menuRepository, GetterUtil getterUtil) {
        this.menuRepository = menuRepository;
        this.getterUtil = getterUtil;
    }

    // Checking a given menu if it is active
    public Boolean isMenuActive(Menu menu) {
        if ((menu.getStartTime().compareTo(LocalTime.now())) <= 0 &&
                (menu.getEndTime().compareTo(LocalTime.now())) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    // Checkin if a menu start and end time interfers with other menus
    public Boolean menuReadyToSave(LocalTime startTime, LocalTime endTime, String managerUsername) {
        List<Menu> menuList = menuRepository.findAllByRestaurantIdAndIsDeleted(getterUtil.getRestaurantId(managerUsername), false);
        for (Menu menu : menuList) {
            if (((startTime.compareTo(menu.getEndTime()) <= 0 && (startTime.compareTo(menu.getStartTime())) >= 0)
                    || startTime.compareTo(endTime) >= 0 ||
                    ((endTime.compareTo(menu.getEndTime())) <= 0 && (endTime.compareTo(menu.getStartTime())) >= 0) ||
                    ((startTime.compareTo(menu.getStartTime())) <= 0 && (endTime.compareTo(menu.getEndTime())) >= 0))) {
                return false;
            }
        }
        return true;
    }
}
