import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    static boolean Exit = false;
    public static void main(String[] args) throws IOException {
        while (!Exit){
            Scanner sc = new Scanner(System.in);
            System.out.println("A NEW GAME!");
            System.out.println("Please select the number of piles(2 or 3):");
            int piles = 0;
            try {
                piles = sc.nextInt();
                if (piles != 2 || piles != 3){
                    System.out.println("Please enter a valid number!");
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter a valid number!");
            }
            Random r = new Random();
            int p1 = r.nextInt(20) + 10;
            int p2 = r.nextInt(20) + 10;
            int p3 = r.nextInt(20) + 10;
            if (piles != 2) Game3Piles(p1, p2, p3,sc);
            else Game2Piles(p1, p2,sc);

        }
    }

    public static void Game2Piles(int p1,int p2,Scanner sc){
        int totalStones = p1 + p2;
        int[][] state = new int[totalStones + 1][totalStones + 1];
        state[1][1] = 0;
        for (int i = 3; i <= totalStones; i++) {
            for (int j = 1; j < i; j++) {
                //remove part of a pile
                for (int k = 1; k < j; k++) {
                    if(state[j - k][i - j] == 0) state[j][i - j] = 1;
                }
                for (int k = 1; k < i - j; k++) {
                    if(state[j][i - j - k] == 0) state[j][i - j] = 1;
                }
                //one pile is all removed
                for (int k = 1; k < j; k++) {
                    if(state[j - k][k] == 0) state[j][i - j] = 1;
                }
                for (int k = 1; k < i - j; k++) {
                    if(state[k][i - j - k] == 0) state[j][i - j] = 1;
                }
            }
        }
        boolean human = true;
        //read the selected pile
        while (p1 != 1 || p2 != 1){
            boolean trans = false;
            if(human){
                System.out.printf("Pile 1: %d stone(s)\n",p1);
                System.out.printf("Pile 2: %d stone(s)\n",p2);
                System.out.println("It's your turn!");
                System.out.println("Please enter the number of pile and the number of stones you want to remove:");
                int choice = 0;
                try {
                    choice = sc.nextInt();
                    if(choice < 1 || choice > 2){
                        System.out.println("Please enter a valid number!");
                        continue;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid number!");
                    continue;
                }
                // read the number of removed stones
                while (true){
                    int remove = 0;
                    if(choice == 1){
                        //the first pile selected
                        try {
                            remove = sc.nextInt();
                            if(remove > p1){
                                System.out.println("You remove TOO MANY stones!");
                                continue;
                            }else if(remove < p1){
                                p1 -= remove;
                                break;
                            }else {
                                //read the transferred stones
                                while (true){
                                    System.out.println("Please enter the number of stones you want to transfer:");
                                    int transfer = 0;
                                    try {
                                        transfer = sc.nextInt();
                                        if(transfer >= p2){
                                            System.out.println("You transfer TOO MANY stones!");
                                            continue;
                                        }else {
                                            p1 = transfer;
                                            p2 -= transfer;
                                            trans = true;
                                            break;
                                        }
                                    }catch (InputMismatchException e) {
                                        System.out.println("You transfer TOO MANY stones!");
                                        continue;
                                    }
                                }
                            }
                        }catch (InputMismatchException e){
                            System.out.println("You remove TOO MANY stones!");
                            continue;
                        }
                    }else {
                        //the second pile selected
                        try {
                            remove = sc.nextInt();
                            if(remove > p2){
                                System.out.println("You remove TOO MANY stones!");
                                continue;
                            }else if(remove < p2){
                                p2 -= remove;
                                break;
                            }else {
                                //read the transferred stones
                                while (true){
                                    System.out.println("Please enter the number of stones you want to transfer:");
                                    int transfer = 0;
                                    try {
                                        transfer = sc.nextInt();
                                        if(transfer >= p1){
                                            System.out.println("You transfer TOO MANY stones!");
                                            continue;
                                        }else {
                                            p2 = transfer;
                                            p1 -= transfer;
                                            trans = true;
                                            break;
                                        }
                                    }catch (InputMismatchException e) {
                                        System.out.println("You transfer TOO MANY stones!");
                                        continue;
                                    }
                                }
                            }
                        }catch (InputMismatchException e){
                            System.out.println("You remove TOO MANY stones!");
                            continue;
                        }
                    }
                    if (trans){
                        break;
                    }
                }
                //piles are updated in the loop above
                human = false;
            }else {
                System.out.printf("Pile 1: %d stone(s)\n",p1);
                System.out.printf("Pile 2: %d stone(s)\n",p2);
                System.out.println("It's computer's turn!");
                if(state[p1][p2] == 1){//winning-position
                    boolean rm1 = false;
                    for (int i = 1; i < p1; i++) {
                        if(state[i][p2] == 0){
                            System.out.printf("Computer removes %d stone(s) from pile 1\n",p1 - i);
                            p1 = i;
                            rm1 = true;
                            break;
                        }
                    }
                    boolean rm2 = rm1;
                    if (!rm1){
                        for (int i = 1; i < p2; i++) {
                            if(state[p1][i] == 0){
                                System.out.printf("Computer removes %d stone(s) from pile 2\n",p2 - i);
                                p2 = i;
                                rm2 = true;
                                break;
                            }
                        }
                    }
                    boolean clr1 = rm2;
                    if(!rm2){
                        for (int i = 1; i < p2; i++) {
                            if(state[i][p2 - i] == 0){
                                System.out.println("Computer removes pile 1");
                                System.out.printf("Computer transfers %d stone(s) from pile 2 to pile 1\n",i);
                                p1 = i;
                                p2 -= i;
                                clr1 = true;
                                break;
                            }
                        }
                    }
                    if(!clr1){
                        for (int i = 1; i < p1; i++) {
                            if(state[p1 - i][i] == 0){
                                System.out.println("Computer removes pile 2");
                                System.out.printf("Computer transfers %d stone(s) from pile 1 to pile 2\n",i);
                                p1 -= i;
                                p2 = i;
                                break;
                            }
                        }
                    }
                }else {//losing position
                    Random r = new Random();
                    int P = r.nextInt(100) % 2;
                    if(P == 1){
                        int remove = r.nextInt(p1 - 1) + 1;
                        System.out.printf("Computer removes %d stone(s) from pile 1\n",remove);
                        p1 -= remove;
                    }else {
                        int remove = r.nextInt(p2 - 1) + 1;
                        System.out.printf("Computer removes %d stone(s) from pile 2\n",remove);
                        p2 -= remove;
                    }
                }
                human = true;
            }
        }
        if(human){
            System.out.println("You LOSE!");
        }else {
            System.out.println("You WIN!");
        }
        System.out.println("Enter 1 for a NEW GAME or 0 to EXIT");
        while (true){
            try {
                int NG = sc.nextInt();
                if(NG == 0){
                    Exit = true;
                    break;
                }
                if (NG != 1){
                    System.out.println("Please enter a VALID number");
                }else {
                    break;
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter a VALID number");
            }
        }

    }

    public static void Game3Piles(int p1,int p2,int p3,Scanner sc){
        int totalStones = p1 + p2 + p3;
        int[][][] state = new int[totalStones + 1][totalStones + 1][totalStones + 1];
        state[1][1][1] = 0;
        for (int i = 4; i <= totalStones; i++) {
            for (int j = 1; j < i - 1; j++) {
                //remove part of a pile
                for (int k = 1; k < i - j; k++) {
                    for (int l = 1; l < j; l++) {
                        if(state[j - l][k][i - j - k] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < k; l++) {
                        if(state[j][k - l][i - j - k] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < i - j - k; l++) {
                        if(state[j][k][i - j - k - l] == 0) state[j][k][i - j - k] = 1;
                    }
                    //one pile is all removed
                    for (int l = 1; l < j; l++) {
                        if(state[j - l][k][l] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < k; l++) {
                        if(state[j][k - l][l] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < j; l++) {
                        if(state[j - l][l][i - j - k] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < i - j - k; l++) {
                        if(state[j][l][i - j - k - l] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < k; l++) {
                        if(state[l][k - l][i - j - k] == 0) state[j][k][i - j - k] = 1;
                    }
                    for (int l = 1; l < i - j - k; l++) {
                        if(state[l][k][i - j - k - l] == 0) state[j][k][i - j - k] = 1;
                    }
                }
            }
        }
        boolean human = true;
        //read the selected pile
        while (p1 != 1 || p2 != 1 || p3 != 1){
            boolean trans = false;
            if(human){
                System.out.printf("Pile 1: %d stone(s)\n",p1);
                System.out.printf("Pile 2: %d stone(s)\n",p2);
                System.out.printf("Pile 3: %d stone(s)\n",p3);
                System.out.println("It's your turn!");
                System.out.println("Please enter the number of pile and the number of stones you want to remove:");
                int choice = 0;
                try {
                    choice = sc.nextInt();
                    if(choice < 1 || choice > 3){
                        System.out.println("Please enter a valid number!");
                        continue;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid number!");
                    continue;
                }
                // read the number of removed stones
                while (true){
                    int remove = 0;
                    if(choice == 1){
                        //the first pile selected
                        try {
                            remove = sc.nextInt();
                            if(remove > p1){
                                System.out.println("You remove TOO MANY stones!");
                                continue;
                            }else if(remove < p1){
                                p1 -= remove;
                                break;
                            }else {
                                //read the transferred stones and pile
                                while (true){
                                    System.out.println("Please enter the pile and the number of stones you want to transfer:");
                                    int transfer = 0;
                                    int trFrom = 0;
                                    try {
                                        trFrom = sc.nextInt();
                                        if(trFrom != 2 && trFrom != 3){
                                            System.out.println("Please enter a valid number!");
                                            continue;
                                        }
                                    }catch (InputMismatchException e){
                                        System.out.println("Please enter a valid number!");
                                        continue;
                                    }
                                    try {
                                        transfer = sc.nextInt();
                                        switch (trFrom){
                                            case 2:
                                                if(transfer >= p2){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p1 = transfer;
                                                    p2 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                            case 3:
                                                if(transfer >= p3){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p1 = transfer;
                                                    p3 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                        }
                                        break;
                                    }catch (InputMismatchException e) {
                                        System.out.println("You transfer TOO MANY stones!");
                                        continue;
                                    }
                                }
                            }
                        }catch (InputMismatchException e){
                            System.out.println("You remove TOO MANY stones!");
                            continue;
                        }
                    }else if(choice == 2){
                        //the second pile selected
                        try {
                            remove = sc.nextInt();
                            if(remove > p2){
                                System.out.println("You remove TOO MANY stones!");
                                continue;
                            }else if(remove < p2){
                                p2 -= remove;
                                break;
                            }else {
                                //read the transferred stones
                                while (true){
                                    System.out.println("Please enter the pile and the number of stones you want to transfer:");
                                    int transfer = 0;
                                    int trFrom = 0;
                                    try{
                                        trFrom = sc.nextInt();
                                        if(trFrom != 1 && trFrom != 3){
                                            System.out.println("Please enter a valid number!");
                                            continue;
                                        }
                                    }catch (InputMismatchException e){
                                        System.out.println("Please enter a valid number!");
                                        continue;
                                    }
                                    try {
                                        transfer = sc.nextInt();
                                        switch (trFrom){
                                            case 1:
                                                if(transfer >= p1){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p2 = transfer;
                                                    p1 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                            case 3:
                                                if(transfer >= p3){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p2 = transfer;
                                                    p3 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                        }
                                        break;
                                    }catch (InputMismatchException e) {
                                        System.out.println("You transfer TOO MANY stones!");
                                        continue;
                                    }
                                }
                            }
                        }catch (InputMismatchException e){
                            System.out.println("You remove TOO MANY stones!");
                            continue;
                        }
                    }else {
                        //the third pile selected
                        try {
                            remove = sc.nextInt();
                            if(remove > p3){
                                System.out.println("You remove TOO MANY stones!");
                                continue;
                            }else if(remove < p3){
                                p3 -= remove;
                                break;
                            }else {
                                //read the transferred stones
                                while (true){
                                    System.out.println("Please enter the pile and the number of stones you want to transfer:");
                                    int transfer = 0;
                                    int trFrom = 0;
                                    try{
                                        trFrom = sc.nextInt();
                                        if(trFrom != 1 && trFrom != 2){
                                            System.out.println("Please enter a valid number!");
                                            continue;
                                        }
                                    }catch (InputMismatchException e){
                                        System.out.println("Please enter a valid number!");
                                        continue;
                                    }
                                    try {
                                        transfer = sc.nextInt();
                                        switch (trFrom){
                                            case 1:
                                                if(transfer >= p1){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p3 = transfer;
                                                    p1 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                            case 2:
                                                if(transfer >= p2){
                                                    System.out.println("You transfer TOO MANY stones!");
                                                    continue;
                                                }else {
                                                    p3 = transfer;
                                                    p2 -= transfer;
                                                    trans = true;
                                                    break;
                                                }
                                        }
                                        break;
                                    }catch (InputMismatchException e) {
                                        System.out.println("You transfer TOO MANY stones!");
                                        continue;
                                    }
                                }
                            }
                        }catch (InputMismatchException e){
                            System.out.println("You remove TOO MANY stones!");
                            continue;
                        }
                    }
                    if (trans){
                        break;
                    }
                }
                //piles are updated in the loop above
                human = false;
            }else {
                System.out.printf("Pile 1: %d stone(s)\n",p1);
                System.out.printf("Pile 2: %d stone(s)\n",p2);
                System.out.printf("Pile 3: %d stone(s)\n",p3);
                System.out.println("It's computer's turn!");
                if(state[p1][p2][p3] == 1){//winning-position
                    boolean rm1 = false;
                    for (int i = 1; i < p1; i++) {
                        if(state[i][p2][p3] == 0){
                            System.out.printf("Computer removes %d stone(s) from pile 1\n",p1 - i);
                            p1 = i;
                            rm1 = true;
                            break;
                        }
                    }
                    boolean rm2 = rm1;
                    if (!rm1){
                        for (int i = 1; i < p2; i++) {
                            if(state[p1][i][p3] == 0){
                                System.out.printf("Computer removes %d stone(s) from pile 2\n",p2 - i);
                                p2 = i;
                                rm2 = true;
                                break;
                            }
                        }
                    }
                    boolean rm3 = rm2;
                    if (!rm2){
                        for (int i = 1; i < p3; i++) {
                            if(state[p1][p2][i] == 0){
                                System.out.printf("Computer removes %d stone(s) from pile 3\n",p3 - i);
                                p3 = i;
                                rm3 = true;
                                break;
                            }
                        }
                    }
                    boolean clr1Tr2 = rm3;
                    if(!rm3){
                        for (int i = 1; i < p2; i++) {
                            if(state[i][p2 - i][p3] == 0){
                                System.out.println("Computer removes pile 1");
                                System.out.printf("Computer transfers %d stone(s) from pile 2 to pile 1\n",i);
                                p1 = i;
                                p2 -= i;
                                clr1Tr2 = true;
                                break;
                            }
                        }
                    }
                    boolean clr1Tr3 = clr1Tr2;
                    if(!clr1Tr2){
                        for (int i = 1; i < p3; i++) {
                            if(state[i][p2][p3 - i] == 0){
                                System.out.println("Computer removes pile 1");
                                System.out.printf("Computer transfers %d stone(s) from pile 3 to pile 1\n",i);
                                p1 = i;
                                p3 -= i;
                                clr1Tr3 = true;
                                break;
                            }
                        }
                    }
                    boolean clr2Tr1 = clr1Tr3;
                    if(!clr1Tr3){
                        for (int i = 1; i < p1; i++) {
                            if(state[p1 - i][i][p3] == 0){
                                System.out.println("Computer removes pile 2");
                                System.out.printf("Computer transfers %d stone(s) from pile 1 to pile 2\n",i);
                                p2 = i;
                                p1 -= i;
                                clr2Tr1 = true;
                                break;
                            }
                        }
                    }
                    boolean clr2Tr3 = clr2Tr1;
                    if(!clr2Tr1){
                        for (int i = 1; i < p3; i++) {
                            if(state[p1][i][p3 - i] == 0){
                                System.out.println("Computer removes pile 2");
                                System.out.printf("Computer transfers %d stone(s) from pile 3 to pile 2\n",i);
                                p2 = i;
                                p3 -= i;
                                clr2Tr3 = true;
                                break;
                            }
                        }
                    }
                    boolean clr3Tr1 = clr2Tr3;
                    if(!clr2Tr3){
                        for (int i = 1; i < p1; i++) {
                            if(state[p1 - i][p2][i] == 0){
                                System.out.println("Computer removes pile 3");
                                System.out.printf("Computer transfers %d stone(s) from pile 1 to pile 3\n",i);
                                p3 = i;
                                p1 -= i;
                                clr3Tr1 = true;
                                break;
                            }
                        }
                    }
                    if(!clr3Tr1){
                        for (int i = 1; i < p1; i++) {
                            if(state[p1][p2 - i][i] == 0){
                                System.out.println("Computer removes pile 3");
                                System.out.printf("Computer transfers %d stone(s) from pile 2 to pile 3\n",i);
                                p3 = i;
                                p2 -= i;
                                break;
                            }
                        }
                    }
                }else {//losing position
                    Random r = new Random();
                    int P = r.nextInt(100) % 3;
                    if(P == 0){
                        int remove = r.nextInt(p1 - 1) + 1;
                        System.out.printf("Computer removes %d stone(s) from pile 1\n",remove);
                        p1 -= remove;
                    }else if(P == 1){
                        int remove = r.nextInt(p2 - 1) + 1;
                        System.out.printf("Computer removes %d stone(s) from pile 2\n",remove);
                        p2 -= remove;
                    }else {
                        int remove = r.nextInt(p3 - 1) + 1;
                        System.out.printf("Computer removes %d stone(s) from pile 3\n",remove);
                        p3 -= remove;
                    }
                }
                human = true;
            }
        }
        if(human){
            System.out.println("You LOSE!");
        }else {
            System.out.println("You WIN!");
        }
        System.out.println("Enter 1 for a NEW GAME or 0 to EXIT");
        while (true){
            try {
                int NG = sc.nextInt();
                if(NG == 0){
                    Exit = true;
                    break;
                }
                if (NG != 1){
                    System.out.println("Please enter a VALID number");
                }else {
                    break;
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter a VALID number");
            }
        }
    }
}
