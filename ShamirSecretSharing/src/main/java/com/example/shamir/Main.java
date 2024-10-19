package com.example.shamir;

import org.json.JSONObject;
import org.json.XML;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (InputStream inputStream = Main.class.getResourceAsStream("/config.xml")) {
            if (inputStream == null) {
                throw new RuntimeException("config.xml not found");
            }

            String xml = new String(inputStream.readAllBytes());
            JSONObject jsonObject = XML.toJSONObject(xml);

            int n = jsonObject.getJSONObject("config").getJSONObject("keys").getInt("n");
            int k = jsonObject.getJSONObject("config").getJSONObject("keys").getInt("k");

            List<Point> points = new ArrayList<>();
            jsonObject.getJSONObject("config").getJSONObject("points").getJSONArray("point").forEach(item -> {
                JSONObject point = (JSONObject) item;
                int x = point.getInt("x");
                int base = point.getInt("base");
                String value = point.getString("value");
                BigInteger y = new BigInteger(value, base);
                points.add(new Point(x, y));
            });

            BigInteger secret = ShamirSecretSharing.findConstantTerm(points, k);
            System.out.println("The constant term is: " + secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
