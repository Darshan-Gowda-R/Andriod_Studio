package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;


//import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture rocket,background,Enemy,blast;
	int Rcount=0,width,hight;
	int rocketY,rocketX,miss,score;
	Boolean forword= true;
	ArrayList<Rectangle> bombRectangle,enemyRectangle;
	ArrayList<Integer> position,E_X_position,Yposition,E_Y_position;
	Random rand;
	String list;
	int E=0,gameState=0,level=1;
	float blastX,blastY;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		background=new Texture("background.png");
		Enemy=new Texture("EnemyC.png");
		rocket = new Texture("rocket-png-40794.png");
		blast=new Texture("blast.png");
		rocketY=0;width=Gdx.graphics.getWidth();hight=Gdx.graphics.getWidth();
		rocketX=0;
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);
		rand=new Random();
		bombRectangle=new ArrayList<>();
		enemyRectangle=new ArrayList<>();
		E_X_position=new ArrayList<>();
		E_Y_position=new ArrayList<>();
		position=new ArrayList<>();
		Yposition=new ArrayList<>();
	}

	void addRocket(int position){


		Yposition.add(0);
		this.position.add(position);

	}

	void createEnemy(){
		int he=320+rand.nextInt(Gdx.graphics.getHeight()-100);
		E_X_position.add(Gdx.graphics.getWidth()+100);
		E_Y_position.add(he);
	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

	if(gameState==0){
		font.draw(batch," TAP TO PLAY !! ",(width-300)/2,(hight-400)/4);
		if(Gdx.input.justTouched()){
			gameState=1;
		}
	}

	if(gameState==1){

		if(Rcount<2){
			Rcount++;
		}else{
			Rcount=0;

			if(rocketX<=width-620 && forword){
				rocketX+=20;
			}
			else if(rocketX>=50 && !forword){
				rocketX-=20;
			}
			else {
				forword = !forword;
			}

		}
		if(E<100){
			E++;
		}
		else{
			E=0;
			createEnemy();
		}
		enemyRectangle.clear();
		for(int i=0;i<E_Y_position.size();i++){
			batch.draw(Enemy,E_X_position.get(i),E_Y_position.get(i));
			E_X_position.set(i,E_X_position.get(i)-20);
			enemyRectangle.add(new Rectangle(E_X_position.get(i),E_Y_position.get(i),Enemy.getWidth(),Enemy.getHeight()));
			if(E_X_position.get(i)<-20){
				miss+=1;
				E_X_position.remove(i);
				E_Y_position.remove(i);
				enemyRectangle.remove(i);
			}
		}
		if(miss>=5){
			gameState=2;
		}
		if(score>=level*5){
			level++;
			miss=0;
		}
		batch.draw(rocket,rocketX,rocketY-100);
		bombRectangle.clear();
		for(int i=0;i<position.size();i++){
			batch.draw(rocket,position.get(i),Yposition.get(i));
			bombRectangle.add(new Rectangle(position.get(i),Yposition.get(i),rocket.getWidth(),rocket.getHeight()));
			Yposition.set(i,Yposition.get(i)+20);
			if(Yposition.get(i)>hight){
				position.remove(i);
				Yposition.remove(i);
				bombRectangle.remove(i);
			}

		}
		if(Gdx.input.justTouched()){
			addRocket(rocketX);
		}
		font.draw(batch,"score : "+score,(width-350),(hight+950)/3);
		font.draw(batch,"Level : "+level,100,(hight+950)/3);
		for (int i=0;i<enemyRectangle.size();i++){
			for(int p=0;p<bombRectangle.size();p++){
				if(Intersector.overlaps(enemyRectangle.get(i),bombRectangle.get(p))) {
					blastX=(float)enemyRectangle.get(i).getX();
					blastY=(float)enemyRectangle.get(i).getY();
					list=enemyRectangle.toString();
					//bombRectangle.remove(p);
					for(int d=0;d<=10;d++)
						batch.draw(blast,blastX,blastY);

					score+=1;
					E_X_position.remove(i);
					E_Y_position.remove(i);
					enemyRectangle.remove(i);
					position.remove(p);
					Yposition.remove(p);
					bombRectangle.remove(p);
					enemyRectangle.clear();
					bombRectangle.clear();
					//gameState=2;
				}
			}
		}


	}
	if(gameState==2){
		font.draw(batch,"GAME OVER BRO!!",(width-300)/2,(hight-50)/4);
		font.draw(batch,"score ! :"+score,(width+200)/2,(hight-400)/4);

		if(Gdx.input.justTouched()){
			bombRectangle.clear();
			enemyRectangle.clear();
			position.clear();
			Yposition.clear();
			E_X_position.clear();
			E_Y_position.clear();
			blastY=0;
			blastX=0;
			gameState=0;
			score=0;
			miss=0;
			level=0;

		}
	}

	batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
