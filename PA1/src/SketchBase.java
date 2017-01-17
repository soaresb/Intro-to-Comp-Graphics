//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)
//	 
//	 September 2016 Brian Soares - added code for drawLine which draws lines and has the ability to smoothly fill a change of colors
//									added code for drawTriangle which allows the user to draw triangles of single color and 
//									a gradient fill of a multiple color triangle.


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//import Commons.Langang;
public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	// draw a line segment
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
	{
		int xStart = p1.x, yStart = p1.y, xEnd = p2.x, yEnd = p2.y;
		int dx = xEnd - xStart, dy = yEnd-yStart;
		//float dg = (p1.c.g - p2.c.g), db = (p1.c.b - p2.c.b), dr = (int) (p1.c.r - p2.c.r);
		float r,g,b;
		int steps;
		float deltaX, deltaY;
		float x = xStart, y = yStart;
		//float r = rStart, g = gStart, b = bStart;
		;
		
		if (Math.abs(dx) > Math.abs(dy)){
			steps = Math.abs(dx);
		}
		else{
			steps = Math.abs(dy);
		}
		
		deltaX = (float) dx / steps;
		deltaY = (float) dy / steps;
		
		
		drawPoint(buff,new Point2D(Math.round(x), Math.round(y),p1.c));
		for(int i =0; i< steps; i++){
			x += deltaX;
			y += deltaY;
			//here to the color interpolation of the 2 points given
			//fsteps is how many steps (in x or y) we will be taking to draw points
			float fsteps = ((float)i/steps);
			r = (p2.c.r-p1.c.r)*(fsteps) + p1.c.r;
			g = (p2.c.g-p1.c.g)*(fsteps) + p1.c.g;
			b = (p2.c.b-p1.c.b)*(fsteps) + p1.c.b;
			//create a new color with the interpolated color for the newly drawn point
			ColorType color = new ColorType(r,g,b);
			//j+=1;
			drawPoint(buff, new Point2D(Math.round(x), Math.round(y), color));
		}
		//drawPoint(buff, p2);
	}
	//Referenced from sunshine2k.de Rasterization algorithm for filling triangles
	public static void fillBottomFlatTriangle(BufferedImage buff,Point2D v1, Point2D v2, Point2D v3)
	{
		
		float invslope1 = (float)(v2.x - v1.x) / (v2.y - v1.y);
		float invslope2 = (float)(v3.x - v1.x) / (v3.y - v1.y);
		//System.out.printf("%d,%d\n",(v2.y-v1.y),(v3.y-v1.y));
		float curx1 = v1.x;
		float curx2 = v1.x;
		float stepps = Math.abs(v2.y - v1.y);
		int j = 0;
		float r,g,b;
		float rr,gg,bb;
		for (int scanlineY = v1.y; scanlineY <= v2.y; scanlineY++)
		{
			float fsteps = ((float)j/stepps);
			//color interpolate over the first non zero slope of the triangle
			r = (v2.c.r-v1.c.r)*(fsteps) + v1.c.r;
			g = (v2.c.g-v1.c.g)*(fsteps) + v1.c.g;
			b = (v2.c.b-v1.c.b)*(fsteps) + v1.c.b;
			
			//System.out.printf("%f\n",v2.c.r-v1.c.r);
			ColorType color = new ColorType(r,g,b);
			//color interpolate over the second non zero slope of the triangle
			rr = (v3.c.r-v1.c.r)*(fsteps) + v1.c.r;
			gg = (v3.c.g-v1.c.g)*(fsteps) + v1.c.g;
			bb = (v3.c.b-v1.c.b)*(fsteps) + v1.c.b;
			ColorType color2 = new ColorType(rr,gg,bb);
			//create 2 new points using the 2 new colors calculated
			Point2D new1 = new Point2D((int)curx1,scanlineY,color);
			Point2D new2 = new Point2D((int)curx2,scanlineY,color2);
			//drawLine using the points above
			drawLine(buff,new1,new2);
			//increase curx1 and curx2 to traverse down the slope of the lines
			curx1 += invslope1;
			curx2 += invslope2;
			j+=1;
		}
	}
	//Referenced from sunshine2k.de Rasterization algorithm for filling triangles
	public static void fillTopFlatTriangle(BufferedImage buff,Point2D v1, Point2D v2, Point2D v3)
	{
	  float invslope1 = (float)(v3.x - v1.x) / (v3.y - v1.y);
	  float invslope2 = (float)(v3.x - v2.x) / (v3.y - v2.y);
	  float stepps = Math.abs(v3.y - v1.y);
	  float curx1 = v3.x;
	  float curx2 = v3.x;
	  float r,g,b;
	  float rr,gg,bb;
	  int j = (int)stepps;
	  //System.out.printf("%f,%f,%f\n",v1.c.r,v2.c.r,v3.c.r);
	  //System.out.printf("%f,%f,%f\n",v1.c.g,v2.c.g,v3.c.g);
	  for (int scanlineY = v3.y; scanlineY > v1.y; scanlineY--)
	  {
		  float fsteps = ((float)j/stepps);
		  //color interpolation
		  r = (float)(v3.c.r-v1.c.r)*(fsteps) + v1.c.r;
		  g = (float)(v3.c.g-v1.c.g)*(fsteps) + v1.c.g;
		  b = (float)(v3.c.b-v1.c.b)*(fsteps) + v1.c.b;
		  ColorType color = new ColorType(r,g,b);
		  //color interpolation
		  rr = (float)(v3.c.r-v2.c.r)*(fsteps) + v2.c.r;
		  gg = (float)(v3.c.g-v2.c.g)*(fsteps) + v2.c.g;
		  bb = (float)(v3.c.b-v2.c.b)*(fsteps) + v2.c.b;
		  ColorType color2 = new ColorType(rr,gg,bb);
			//System.out.printf("%f,%f,%f\n",color.r,color.b,color.g);
		  Point2D new1 = new Point2D((int)curx1,scanlineY,color);
		  Point2D new2 = new Point2D((int)curx2,scanlineY,color2);
		  drawLine(buff,new1,new2);
		  //traverse up the slope by decreasing curx1 and curx2 by their respective slopes
		  curx1 -= invslope1;
		  curx2 -= invslope2;
		  j-=1;
	  }
	}
	// draw a triangle
	////Referenced from sunshine2k.de Rasterization algorithm for filling triangles
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		int [] sorted = new int[3];
		Point2D v1 = new Point2D();
		Point2D v2 = new Point2D();
		Point2D v3 = new Point2D();
		sorted[0] = p1.y;
		sorted[1] = p2.y;
		sorted[2] = p3.y;
		float r,g,b;
		float rr,gg,bb;		
		Arrays.sort(sorted);
		//sort the points so that v1 has the highest y value
		//v2 has the medium y value
		//and v3 has the lowest y value
		if(sorted[0]==p1.y)
		{
			v1 = p1;
			if(sorted[1]==p2.y){
				v2=p2;
				v3=p3;
			}
			else
			{
				v2=p3;
				v3=p2;
			}
		}
		else if(sorted[0]==p2.y)
		{
			v1=p2;
			if(sorted[1]==p1.y){
				v2=p1;
				v3=p3;}
			else{
				v2=p3;
				v3=p1;
			}
		}
		else if(sorted[0]==p3.y)
		{
			v1=p3;
			if(sorted[1]==p1.y){
				v2=p1;
				v3=p2;
			}
			else{
				v2=p2;
				v3=p1;
			}
				
		}
		//if not smooth curve 
		//fill the triangle with the color of the first point
		 if(!do_smooth)
		    {
		    	v1.c.r = p1.c.r;
		    	v1.c.b = p1.c.b;
		    	v1.c.g = p1.c.g;
		    	v2.c.r = p1.c.r;
		    	v2.c.g = p1.c.g;
		    	v2.c.b = p1.c.b;
		    	v3.c.r = p1.c.r;
		    	v3.c.g = p1.c.g;
		    	v3.c.b = p1.c.b;
		    	//v4.c = p1.c;
		    	
		    }
		 //check if the triangle is already a flat bottom triangle
		 //if so just call fillBottomFlatTriangle and exit
		if (v2.y == v3.y)
		  {
		    fillBottomFlatTriangle(buff,v1, v2, v3);
		  }
		  /* check for trivial case of top-flat triangle */
		  else if (v1.y == v2.y)
		  {
		    fillTopFlatTriangle(buff, v1, v2, v3);
		  } 
		  else
		  {
		    // split the triangle in a topflat and bottom-flat one 
			 //float newx= v1.x + ((float)(v2.y - v1.y) / (float)(v3.y - v1.y)) * (v3.x - v1.x);
			float deltah = (float)(v3.y-v1.y);
			float delta4 = (float)(v1.y-v2.y);
			float delta3 = (float)(v3.y-v2.y);
			float change1 = Math.abs(delta4/deltah);
			//r = v1.c.r*change1 + 
			
			r = (float)v1.c.r*(v3.y-v2.y)/(v3.y-v1.y) + v3.c.r*(v2.y-v1.y)/(deltah);
			g = (float)v1.c.g*(v3.y-v2.y)/(v3.y-v1.y) + v3.c.g*(v2.y-v1.y)/(deltah);
		    
		    //System.out.printf("%f,%f,%f\n",r+rr,g+gg,b+bb);
		    //System.out.printf("%f\n",g+gg);
		    ColorType color = new ColorType(v3.c.r*(change1)+(v1.c.r)*(1-change1),v3.c.g*(change1)+(v1.c.g)*(1-change1),v3.c.b*(change1)+(v1.c.b)*(1-change1));
		    //create point v4 that stems from v2 in order to create a top and a bottom triangle
		    Point2D v4 = new Point2D( 
		      (int)(v1.x + ((float)(v2.y - v1.y) / (float)(v3.y - v1.y)) * (v3.x - v1.x)), v2.y,color);
		  
		    
		   	drawLine(buff,v1,v3);
		    
		    //if no smoothfill make v4 color equal to color of the first point.
		    if(!do_smooth)
		    {
		    	v4.c.r = p1.c.r;
		    	v4.c.g = p1.c.g;
		    	v4.c.b = p1.c.b;
		    	
		    }
		    fillBottomFlatTriangle(buff, v1, v2, v4);
		    fillTopFlatTriangle(buff, v2, v4, v3);
		    
		  }
		
		//drawPoint(buff, p3);
	}
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawPoint(buff, p3);
	}
}
