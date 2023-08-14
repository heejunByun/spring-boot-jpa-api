package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 준영속 엔티티를 수정하는 2가지 방법 중 > 변경 감지 기능 사용
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); // id 를 기반으로 실제 영속상태 엔티티를 찾아옴
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // 변경을 하고 아무것도 해줄 필요가 없다. > 영속상태이기 때문에 변경감지를 통해 알아서 update 가 된다.
    }

    // 준영속 엔티티를 수정하는 2가지 방법 중 > merge (병합)은 itemController 의 updateItem 이 merge 방법.

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
