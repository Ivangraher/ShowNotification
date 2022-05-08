package edu.uoc.epcsd.notification.controllers;

import edu.uoc.epcsd.notification.pojos.Category;
import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.pojos.User;
import edu.uoc.epcsd.notification.services.NotificationService;
import edu.uoc.epcsd.notification.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    //obtenemos la URL de la petición GET del servicio ShowCatalog.
    //El parametro idShow lo obtenemos de la consulta POST de ShowCatalog cuando creamos un nuevo show
    private final String showCatalogUrl = "http://localhost:18081/show/getShow/{idShow}"; //localhost:18081/show/getShow/1

    //realizamos la inyección de dependencias
    @Autowired
    private NotificationService notificationService;

    //recibimos como parametro el id del show creado
    @PostMapping("/{idShow}")
    public ResponseEntity createNotification(@PathVariable Long idShow) {
        log.trace("sendShowCreated");

        RestTemplate restTemplate = new RestTemplate();
        Show show = restTemplate.getForObject(showCatalogUrl, Show.class, idShow);

        //comprobamos que el objeto show existe, si existe creamos la notificación, si no existe, no se crea la notificación
        if (show != null) {
            notificationService.notifyShowCreation(show);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
