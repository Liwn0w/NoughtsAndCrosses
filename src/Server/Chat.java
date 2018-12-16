package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat {
        private List<String> chatLines = Collections.synchronizedList(new ArrayList<String>());

        public Chat() {

        }

        public void addComment(String comment) { chatLines.add(comment); }
        public int getSize() { return chatLines.size(); }
        public String getComment(int n) { return chatLines.get(n); }
}

