import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Terrain extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    //Assets
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    
    //variable des image des blocs de couleurs, du fond d'écran du jeu, du button pause et recommencer
    private BufferedImage blocs, fond, pause, recommencer;

    //Variable constantes de la hauteur et de la largeur du terrain
    private final int terrainHauteur = 20, terrainLargeur = 10;

    //variable de la taille de un bloc = la taille du carré d'une couleur d'une pièce du tetris
    private final int blocTaille = 30;

    // double tableau de int qui représente le terrain de jeu de 20 de haut et 10 de large
    private int[][] terrain = new int[terrainHauteur][terrainLargeur];

    // boucle du jeu. timer permet de rafraichir et de repeindre le jeu 
    private Timer timer;
    // FPS = image par seconde 60 est une vitessse normale
    private int FPS = 60;
    //60 fois par seconde c'est le délais
    private int delais = 1000 / FPS;

    // mouse events variables
    private int mouseX, mouseY;

    private boolean clicGauche = false;


    private Rectangle bordurePause, bordureRelancer;

    private boolean gamePaused = false;

    private boolean gameOver = false;

    // buttons press lapse
    

    // score
    private int score = 0;

    public Terrain() {
        // load Assets
        File cheminRelatif = new File(".");
        try {
            System.out.println("Le chemin relatif est : " + cheminRelatif.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(Titre.class.getName()).log(Level.SEVERE, null, ex);
        }


        blocs = ChageurImage.chargeImage( cheminRelatif + "\\tetrominos.png");

        fond = ChageurImage.chargeImage(cheminRelatif +"\\fond.png");
        pause = ChageurImage.chargeImage(cheminRelatif + "\\pause.png");
        recommencer = ChageurImage.chargeImage(cheminRelatif + "\\rafraichir.png");

        ChargeurSon musique = new ChargeurSon( cheminRelatif + "\\music.wav");
        
        musique.loop();
        mouseX = 0;
        mouseY = 0;

        bordurePause = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight() / 2);
        bordureRelancer = new Rectangle(350, 500 - recommencer.getHeight() - 20, recommencer.getWidth(),
                recommencer.getHeight() + recommencer.getHeight() / 2);

        // création de la bloucle du jeu
        timer = new Timer(delais, new BoucleJeu());
    }

    private void miseAJour() {
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fond, 0, 0, null);

        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {
                //on peut dessiner l'image du carré d'une forme d'une tetris seulement s'il la 
                //du terrain a la ligne et a la colonne est différente de 0 
                if (terrain[row][col] != 0) {

                    //(terrain[row][col] - 1) * blocTaille permet de sélectionner une couleurs parmi celles de 
                    //l'image tiles -1 car index on commence a 0 et non a 1
                    g.drawImage(blocs.getSubimage((terrain[row][col] - 1) * blocTaille,
                            0, blocTaille, blocTaille), col * blocTaille, row * blocTaille, null);
                }

            }
        }
        

        //afffichage du bouton pour mettre en pause la partie
        if (bordurePause.contains(mouseX, mouseY)) {
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT),
                    bordurePause.x + 3, bordurePause.y + 3, null);
        } else {
            g.drawImage(pause, bordurePause.x, bordurePause.y, null);
        }

        //affichage du bouton pour relancer une partie
        if (bordureRelancer.contains(mouseX, mouseY)) {
            g.drawImage(recommencer.getScaledInstance(recommencer.getWidth() + 3, recommencer.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), bordureRelancer.x + 3, bordureRelancer.y + 3, null);
        } else {
            g.drawImage(recommencer, bordureRelancer.x, bordureRelancer.y, null);
        }

        //si on a cliquer sur le bouton pause on met en pause le jeu
        if (gamePaused) {
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 35, Ecran.HAUTEUR / 2);
        }
        //si on a perdu on dessine game over en blanc au milieu du terrain
        if (gameOver) {
            
        }
        g.setColor(Color.RED);

        g.setFont(new Font("Georgia", Font.BOLD, 20));

        g.drawString("SCORE", Ecran.LARGEUR - 125, Ecran.HAUTEUR / 2);
        g.drawString(score + "", Ecran.LARGEUR - 125, Ecran.HAUTEUR / 2 + 30);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(0, 0, 0, 100));

        // on dessine les lignes horizontales qui délimite le terrain en hauteur = 20 
        for (int i = 0; i <= terrainHauteur; i++) {
            g2d.drawLine(0, i * blocTaille, terrainLargeur * blocTaille, i * blocTaille);
        }
        // on dessine les lignes verticales qui délimite le terrain en largeur = 10 
        for (int j = 0; j <= terrainLargeur; j++) {
            g2d.drawLine(j * blocTaille, 0, j * blocTaille, terrainHauteur * 30);
        }
    }

    public void setProchaineForme() {
        
    }

    public void setFormeCourante() {
        

    }

    public int[][] getTerrain() {
        return terrain;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void lancerPartie() {
        arreterPartie();
        setProchaineForme();
        setFormeCourante();
        gameOver = false;
        timer.start();

    }

    public void arreterPartie() {
        score = 0;

        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {
                terrain[row][col] = 0;
            }
        }
        timer.stop();
    }

    class BoucleJeu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            miseAJour();
            repaint();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicGauche = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicGauche = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void ajouterScore() {
        score += 100;
    }

}
