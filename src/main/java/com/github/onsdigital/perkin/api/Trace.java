package com.github.onsdigital.perkin.api;

import com.github.davidcarboni.restolino.framework.Api;
import com.github.onsdigital.perkin.transform.Audit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.List;

@Api
public class Trace {

    @GET
    public List<String> get(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return Audit.getInstance().getMessages();
    }
}
