package com.example;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {

    private Long count;
    private List<VkontakteFriend> items;

    public List<VkontakteFriend> getItems() {
        return items;
    }
}
