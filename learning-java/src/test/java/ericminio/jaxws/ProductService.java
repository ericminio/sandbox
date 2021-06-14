package ericminio.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class ProductService {

    @WebMethod
    public Product find(int key) {
        return new Product("this-name");
    }
}
