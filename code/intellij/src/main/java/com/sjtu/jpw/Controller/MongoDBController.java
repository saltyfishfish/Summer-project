package com.sjtu.jpw.Controller;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.sjtu.jpw.Service.MongoDBService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Controller
public class MongoDBController {

    @Resource(name="mongoDBService")
    private MongoDBService mongoDBService;

    @RequestMapping(value="/uploadImg",produces="application/json;charset=UTF-8")
    public void UploadAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","application/json;charset=UTF-8");
    }

    @RequestMapping(value="/addAvatar",produces="application/json;charset=UTF-8")
    public void AddAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","application/json;charset=UTF-8");

        DBCollection collection = mongoDBService.getCollection("avatar");
        String imgUrl = request.getParameter("imgUrl");
        String userId = request.getParameter("userId");
        DBObject dbObject = new BasicDBObject();
        BasicDBObject query = new BasicDBObject();
        if(collection.findOne(query)!=null){
            collection.remove(query);
        }
        dbObject.put("userId",userId);
        dbObject.put("imgUrl",imgUrl);
        collection.insert(dbObject);
    }

    @RequestMapping(value="/getAvatar",produces="application/json;charset=UTF-8")
    public void GetAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();

        DBCollection collection = mongoDBService.getCollection("avatar");
        BasicDBObject query = new BasicDBObject();
        query.put("userId","1");
        DBObject img = collection.findOne(query);
        System.out.println(img);
        if(img != null){
            System.out.println(img.get("imgUrl"));
            out.print(img.get("imgUrl"));
        }
        out.flush();

    }

    @RequestMapping(value="/addImage",produces="application/json;charset=UTF-8")
    public void AddImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","application/json;charset=UTF-8");

        DBCollection collection = mongoDBService.getCollection("image");
        String imgUrl = request.getParameter("imgUrl");
        String showId = request.getParameter("showId");
        DBObject dbObject = new BasicDBObject();
        dbObject.put("showId",showId);
        dbObject.put("imgUrl",imgUrl);
        collection.insert(dbObject);

    }

    @RequestMapping(value="/getImage",produces="application/json;charset=UTF-8")
    public void GetImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","multipart/form-data;charset=UTF-8");

        PrintWriter out = response.getWriter();

        DBCollection collection = mongoDBService.getCollection("image");
//        BasicDBObject query = new BasicDBObject();
//        query.put("showId","1");
        DBObject img = collection.findOne();
        if(img!=null) {
            System.out.println(img.get("imgUrl"));
            out.print(img.get("imgUrl").toString());
            out.flush();
        }

    }
}