package com.appost.Controller;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
 
@Controller
public class HtmlControler {

    @GetMapping("/deleteMyAccountPage")
    public String deleteMyAccountPage() {
        return "DeleteAccount";
    }

    @GetMapping("/")
    public String index() {return "index";}
    
}
