package ru.practicum.shareit.item.repository;

public class ItemRepositoryImpl {
//
//    private final Map<Long, Item> items = new HashMap<>();
//    private long count = 1;
//
//    @Override
//    public Item save(Item item) {
//        item.setId(counter());
//        items.put(item.getId(), item);
//        return item;
//    }
//
//    @Override
//    public Item getItemById(long itemId) {
//        if (items.containsKey(itemId)) {
//            return items.get(itemId);
//        } else {
//            throw new NotFoundException("Item with this id:" + itemId + " doesn't exists");
//        }
//    }
//
//    @Override
//    public List<Item> getItemList(long userId) {
//        return items.values().stream()
//                .filter(item -> item.getOwner() == userId)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Item updateItem(long itemId, Item item) {
//        if (items.get(itemId).getOwner() != item.getOwner()) {
//            throw new NotFoundException("Wrong userId");
//        }
//        Item oldItem = items.get(itemId);
//        if (item.getName() != null) {
//            oldItem.setName(item.getName());
//        }
//        if (item.getDescription() != null) {
//            oldItem.setDescription(item.getDescription());
//        }
//        if (item.getAvailable() != null) {
//            oldItem.setAvailable(item.getAvailable());
//        }
//        items.put(oldItem.getId(), oldItem);
//        return oldItem;
//    }
//
//    @Override
//    public List<Item> searchItem(String text) {
//        if (text.isBlank()) {
//            return new ArrayList<>();
//        }
//        return items.values().stream()
//                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
//                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
//                        && item.getAvailable()).collect(Collectors.toList());
//    }
//
//    private long counter() {
//        return count++;
//    }
}