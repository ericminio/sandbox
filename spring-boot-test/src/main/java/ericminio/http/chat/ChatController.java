package ericminio.http.chat;

import ericminio.domain.chat.Group;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ChatController {

    @RequestMapping(method = POST, value = "/apply")
    public Group apply(@RequestBody Data data) {
        return data.getExclusion().visit(data.getGroup());
    }
}
