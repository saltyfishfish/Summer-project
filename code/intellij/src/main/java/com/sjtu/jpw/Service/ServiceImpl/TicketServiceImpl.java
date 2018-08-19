package com.sjtu.jpw.Service.ServiceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjtu.jpw.Domain.Collection;
import com.sjtu.jpw.Domain.Shows;
import com.sjtu.jpw.Domain.Ticket;
import com.sjtu.jpw.Repository.CollectionRepository;
import com.sjtu.jpw.Repository.CommentRepository;
import com.sjtu.jpw.Repository.ShowsRepository;
import com.sjtu.jpw.Repository.TicketRepository;
import com.sjtu.jpw.Domain.AssistDomain.ShowTicket;
import com.sjtu.jpw.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private ShowsRepository showsRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CollectionRepository collectionRepository;


    @Override
    public String allTickets(String city, String type, Timestamp startTime, Timestamp endTime,
                             String search, Integer userId) {

        List<Integer> showsLike = collectionRepository.findAllShowCollectionId(userId);
        List<ShowTicket> itemList = showsRepository.findAllShowsByParams(city, type, startTime, endTime, search);
        System.out.println(itemList.size());
        for (int i = 0; i < itemList.size(); i++) {
            ShowTicket temp = itemList.get(i);
            Integer showId = setShowTicketInfo(temp);
            if (showsLike.contains(showId)) {
                temp.setIsLike(true);
            }
            System.out.println(temp.toString());
        }


        Gson gson = new Gson();
        return gson.toJson(itemList);
    }

    private Integer setShowTicketInfo(ShowTicket temp) {
        Integer showId = temp.getShowId();
        Integer commentNum = commentRepository.countByShowId(showId);
        temp.setCommentNum(commentNum);
        Integer rate = commentRepository.getRateByShowId(showId);
        temp.setRate(rate);
        Integer minPrice = ticketRepository.minPrice(showId);
        temp.setMinPrice(minPrice);
        Integer stock = ticketRepository.getStock(showId);
        temp.setStock(stock);
        return showId;
    }

    @Override
    public String userCollection(Integer userId) {

        List<ShowTicket> itemList=showsRepository.findCollectionShows(collectionRepository.findAllShowCollectionId(userId));

        for(int i=0;i<itemList.size();i++){
            ShowTicket temp=itemList.get(i);
            setShowTicketInfo(temp);
            temp.setIsLike(true);
            System.out.println(temp.toString());
        }
        Gson gson=new Gson();
        return gson.toJson(itemList);
    }

    @Override
    public JsonArray CartTickets(Integer userId) {


        return null;
    }

    @Override
    public String ticketsDetail(Integer showId)  {

        SimpleDateFormat dateformat =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeformat =  new SimpleDateFormat("HH:mm");
        Map<String,Map<String,List<Ticket>>> ticketMap= new HashMap<>();
        List<Ticket> allTickets=ticketRepository.findAllByShowId(showId);
        for (Ticket ticket : allTickets) {
            String date = dateformat.format(ticket.getTime());
            String time = timeformat.format(ticket.getTime());
            if (ticketMap.containsKey(date)) {
                Map<String,List<Ticket>> tempMap=ticketMap.get(date);
                if (tempMap.containsKey(time)){
                    List<Ticket> tempList= tempMap.get(time);
                    tempList.add(ticket);
                    tempMap.put(time,tempList);
                    ticketMap.put(date,tempMap);
                }
                else{
                    List<Ticket> newList=new ArrayList<>();
                    newList.add(ticket);
                    tempMap.put(time,newList);
                }
            }
            else{
                List<Ticket> newList=new ArrayList<>();
                newList.add(ticket);
                Map<String,List<Ticket>> newMap=new HashMap<>();
                newMap.put(time,newList);
                ticketMap.put(date,newMap);
            }
        }
        Gson gson=new Gson();
        System.out.println(gson.toJson(ticketMap));

        return gson.toJson(ticketMap);





      /*  SimpleDateFormat testformat =  new SimpleDateFormat("HH:mm");
        Timestamp timestamp=Timestamp.valueOf("1970-01-06 11:45:55");
        String date=testformat.format(timestamp);
        System.out.println("test format:"+date);*/

    }

    @Override
    public void addCollection(Integer userId,Integer showId){
        Collection temp=new Collection();
        temp.setShowId(showId);
        temp.setUserId(userId);
        collectionRepository.save(temp);
    }

    @Override
    public void deleteCollection(Integer userId,Integer showId){
        collectionRepository.deleteByShowIdAndUserId(showId,userId);
    }

    @Override
    public void deleteAllCollection(Integer userId){
        collectionRepository.deleteByUserId(userId);
    }

    @Override
    public void addTicket(String title, Integer price, String time, String seat, Integer amount){

    }

    @Override
    public JsonArray getTickets(){
        List<Ticket> tickets = ticketRepository.findAllTickets();
        JsonArray ticketsData = new JsonArray();
        Iterator<Ticket> it = tickets.iterator();
        while(it.hasNext()){
            Ticket ticket = it.next();
            JsonObject ticketObject = new JsonObject();
            String title = showsRepository.findFirstByShowId(ticket.getShowId()).getTitle();
            ticketObject.addProperty("title",title);
            ticketObject.addProperty("price",ticket.getPrice());
            ticketObject.addProperty("time",ticket.getTime().toString());
            ticketObject.addProperty("seat",ticket.getSeat());
            ticketObject.addProperty("amount",ticket.getAmount());
            ticketObject.addProperty("stock",ticket.getStock());
            ticketsData.add(ticketObject);
        }
        return ticketsData;
    }
}
