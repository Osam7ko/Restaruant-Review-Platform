package com.osproject.restaurant.manual;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.Address;
import com.osproject.restaurant.domain.entity.OperatingHours;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.domain.entity.TimeRange;
import com.osproject.restaurant.services.PhotoServices;
import com.osproject.restaurant.services.RestaurantService;
import com.osproject.restaurant.services.impl.RandomRiyadhGeoLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RestaurantDataLoaderTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RandomRiyadhGeoLocationService geoLocationService;

    @Autowired
    private PhotoServices photoService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @Rollback(false) // Allow changes to persist
    public void createSampleRestaurants() throws Exception {
        List<RestaurantCreateUpdateRequest> restaurants = createRestaurantData();
        restaurants.forEach(restaurant -> {
            String fileName = restaurant.getPhotoIds().getFirst();
            Resource resource = resourceLoader.getResource("classpath:testdata/" + fileName);
            MultipartFile multipartFile = null;
            try {
                multipartFile = new MockMultipartFile(
                        "file", // parameter name
                        fileName, // original filename
                        MediaType.IMAGE_PNG_VALUE,
                        resource.getInputStream()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // Call the service method
            PhotoEntity uploadedPhoto = photoService.uploadPhoto(multipartFile);

            restaurant.setPhotoIds(List.of(uploadedPhoto.getUrl()));

            restaurantService.createRestaurant(restaurant);

            System.out.println("Created restaurant: " + restaurant.getName());
        });
    }

    private List<RestaurantCreateUpdateRequest> createRestaurantData() {
        return Arrays.asList(
                createRestaurant(
                        "مطعم التنين الذهبي",
                        "صيني",
                        "+966 50 123 4567",
                        createAddress("12", "شارع التخصصي", null, "الرياض", "منطقة الرياض", "12345", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:30", "23:00", "11:30", "23:30"),
                        "golden-dragon.png"
                ),
                createRestaurant(
                        "لا بيتيت ميزون",
                        "فرنسي",
                        "+966 50 234 5678",
                        createAddress("25", "شارع الملك فهد", null, "الرياض", "منطقة الرياض", "12346", "المملكة العربية السعودية"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "la-petit-maison.png"
                ),
                createRestaurant(
                        "راج بافيليون",
                        "هندي",
                        "+966 50 345 6789",
                        createAddress("8", "طريق العليا", null, "الرياض", "منطقة الرياض", "12347", "المملكة العربية السعودية"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "raj-pavilion.png"
                ),
                createRestaurant(
                        "سوشي ماستر",
                        "ياباني",
                        "+966 50 456 7890",
                        createAddress("33", "طريق الملك عبدالله", null, "الرياض", "منطقة الرياض", "12348", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "sushi-master.png"
                ),
                createRestaurant(
                        "الزيتونة الريفية",
                        "إيطالي",
                        "+966 50 567 8901",
                        createAddress("19", "شارع التحلية", null, "الرياض", "منطقة الرياض", "12349", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "rustic-olive.png"
                ),
                createRestaurant(
                        "إل تورو",
                        "إسباني",
                        "+966 50 678 9012",
                        createAddress("40", "شارع الضباب", null, "الرياض", "منطقة الرياض", "12350", "المملكة العربية السعودية"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "el-toro.png"
                ),
                createRestaurant(
                        "بيت اليونان",
                        "يوناني",
                        "+966 50 789 0123",
                        createAddress("55", "طريق الأمير تركي الأول", null, "الرياض", "منطقة الرياض", "12351", "المملكة العربية السعودية"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "greek-house.png"
                ),
                createRestaurant(
                        "مطبخ سيول",
                        "كوري",
                        "+966 50 890 1234",
                        createAddress("72", "شارع الملك سعود", null, "الرياض", "منطقة الرياض", "12352", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "seoul-kitchen.png"
                ),
                createRestaurant(
                        "تاي أوركيد",
                        "تايلندي",
                        "+966 50 901 2345",
                        createAddress("61", "شارع العليا العام", null, "الرياض", "منطقة الرياض", "12353", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:00", "22:30", "11:00", "23:00"),
                        "thai-orchid.png"
                ),
                createRestaurant(
                        "برجر الرياض",
                        "أمريكي",
                        "+966 50 012 3456",
                        createAddress("88", "طريق مكة المكرمة", null, "الرياض", "منطقة الرياض", "12354", "المملكة العربية السعودية"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "burger-joint.png"
                )
        );
    }


    private RestaurantCreateUpdateRequest createRestaurant(
            String name,
            String cuisineType,
            String contactInformation,
            Address address,
            OperatingHours operatingHours,
            String photoId
    ) {
        return RestaurantCreateUpdateRequest.builder()
                .name(name)
                .cuisineType(cuisineType)
                .contactInformation(contactInformation)
                .address(address)
                .operatingHours(operatingHours)
                .photoIds(List.of(photoId))
                .build();
    }

    private Address createAddress(
            String streetNumber,
            String streetName,
            String unit,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setUnit(unit);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }

    private OperatingHours createStandardOperatingHours(
            String weekdayOpen,
            String weekdayClose,
            String weekendOpen,
            String weekendClose
    ) {
        TimeRange weekday = new TimeRange();
        weekday.setOpenTime(weekdayOpen);
        weekday.setCloseTime(weekdayClose);

        TimeRange weekend = new TimeRange();
        weekend.setOpenTime(weekendOpen);
        weekend.setCloseTime(weekendClose);

        OperatingHours hours = new OperatingHours();
        hours.setMonday(weekday);
        hours.setTuesday(weekday);
        hours.setWednesday(weekday);
        hours.setThursday(weekday);
        hours.setFriday(weekend);
        hours.setSaturday(weekend);
        hours.setSunday(weekend);

        return hours;
    }
}