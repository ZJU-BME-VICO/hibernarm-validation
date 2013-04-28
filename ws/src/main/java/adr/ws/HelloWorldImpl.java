
package adr.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "adr.ws.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

