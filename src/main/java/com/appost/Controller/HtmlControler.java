package com.appost.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
 
@Controller
public class HtmlControler {

    @GetMapping("/deleteMyAccountPage")
    public String deleteMyAccountPage() {
        return "DeleteAccount";
    }

    @GetMapping("/")
    public String index() {return "index";}
    
}
