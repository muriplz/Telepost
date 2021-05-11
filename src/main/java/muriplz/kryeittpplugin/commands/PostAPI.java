package muriplz.kryeittpplugin.commands;

public class PostAPI {
    public static int getNearPostX(int gap,int playerX, int originX){
        int postX=0;
        while(true){
            if(playerX>=gap && playerX>0){
                playerX=playerX-gap;
                postX+=gap;
            }
            else if(playerX<=-gap && playerX<0){
                playerX=playerX+gap;
                postX-=gap;
            }
            else{break;}
        }
        if(playerX>gap/2&&playerX>0){
            postX+=gap;
        }
        if(playerX<-gap/2&&playerX<0){
            postX-=gap;
        }
        postX+=originX;
        return postX;
    }
    public static int getNearPostZ(int gap, int playerZ,int originZ) {
        int postZ=0;
        while(true){
            if(playerZ>=gap && playerZ>0){
                playerZ=playerZ-gap;
                postZ+=gap;
            }
            else if(playerZ<=-gap && playerZ<0){
                playerZ=playerZ+gap;
                postZ-=gap;
            }
            else{break;}
        }
        if(playerZ>gap/2&&playerZ>0){
            postZ+=gap;
        }
        if(playerZ<-gap/2&&playerZ<0){
            postZ-=gap;
        }
        postZ+=originZ;
        return postZ;
    }
}
