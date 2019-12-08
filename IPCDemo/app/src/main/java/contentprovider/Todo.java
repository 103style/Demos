package contentprovider;

/**
 * @author https://github.com/103style
 * @date 2019/11/6 22:27
 */
public class Todo {
    public int _id;
    public String title;
    public int priority;

    @Override
    public String toString() {
        return "Todo{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                '}';
    }
}
