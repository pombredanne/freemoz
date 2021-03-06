package com.freemoz.app.routes;


import com.freemoz.app.dto.*;
import com.freemoz.app.service.EditorService;
import com.freemoz.app.service.Singleton;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

public class ContentRoute {
    public static ModelAndView getCategory(Request request, Response response, String category) {
        Map<String, Object> map = new HashMap<>();

        String searchCategory = category;
        List<String> splat = Arrays.asList(request.splat());
        if (!splat.isEmpty()) {
            searchCategory = category + "/" + String.join("/", splat);
        }

        List<StructureDTO> subcategories = Singleton.getContentDAO().getSubcategories(searchCategory);
        List<ContentDTO> sites = Singleton.getContentDAO().getSites(searchCategory);

        List<BreadCrumbDTO> breadCrumb = new ArrayList<>();
        if (!splat.isEmpty()) {
            String newSplat = "/" + category + "/" + splat.get(0);
            List<String> split = Arrays.asList(newSplat.split("/"));

            for (int i=split.size() -1; i != 0; i--) {
                breadCrumb.add(new BreadCrumbDTO(String.join("/", split.subList(0, i + 1)), split.get(i).replace("_", " ")));
            }
            Collections.reverse(breadCrumb);
        }


        map.put("breadCrumb", breadCrumb);
        map.put("categoryName", category);
        map.put("subCategories", subcategories);
        map.put("sites", sites);

        return new ModelAndView(map, "category.ftl");
    }

    public static List<String> getCategories(Request request, Response response) {
        if (!request.queryParams().contains("q")) {
            return new ArrayList<>();
        }

        return Singleton.getContentDAO().searchCategories(request.queryParams("q")).stream().map(x -> x.getTopic()).collect(Collectors.toList());
    }

    public static SubmissionDTO getSubmission(Request request, Response response) {
        String authenticatedUser = EditorRoute.getAuthenticatedUser(request);
        UserDTO userByUsername = Singleton.getUserDAO().getUserByUsername(authenticatedUser);
        return Singleton.getQueueDAO().getNextSubmission(userByUsername);
    }
}
