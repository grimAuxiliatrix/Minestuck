package com.mraof.minestuck.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class AlchemiterHandler extends TemplateRecipeHandler {

	class CachedAlchemiterRecipe extends CachedRecipe {

		private ItemStack item;
		public CachedAlchemiterRecipe(ItemStack input) {
			this.item = input;
		}
		
		@Override
		public PositionedStack getResult() {
			return new PositionedStack(item,130,9);
		}
		
		
	}

	private GuiDraw fontRenderer = new GuiDraw();
		
	@Override
	public String getRecipeName() {
		return "Alchemiter";
	}

	@Override
	public String getGuiTexture() {
		return "minestuck:textures/gui/alchemiter.png";
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result){
//		for (Object item : GristRegistry.getAllConversions().entrySet()) {
//			Map.Entry entry = (Map.Entry)item;
//			List itemData = (List)entry.getKey();
//			int id = (Integer)itemData.get(0);
//			int meta = (Integer)itemData.get(1);
//			arecipes.add(new CachedAlchemiterRecipe(new ItemStack(id,1,meta)));
//		}
		
		if (GristRegistry.getGristConversion(result) != null) {
			arecipes.add(new CachedAlchemiterRecipe(result));
		}
	}
	
    @Override
    public void drawExtras(int recipe)
    {
    	
    	//render progress bar
    	changeTexture("minestuck:textures/gui/progress/alchemiter.png");
        drawProgressBar(49, 12, 0, 0, 71, 10, 50, 0);
    	
    	//render carved dowel
    	changeTexture("minestuck:textures/items/CruxiteCarved.png");
    	drawTexturedModalRect(22, 9, 0, 0, 16, 16,16,16);
    	
    	//Render grist requirements
    	ItemStack result = arecipes.get(recipe).getResult().item;
    	GristSet set = GristRegistry.getGristConversion(result);
    	
    	if (set == null) {fontRenderer.drawString("Not Alchemizable", 4,34, 16711680); return;}
    	Hashtable reqs = set.getHashtable();
    	//Debug.print("reqs: " + reqs.size());
    	if (reqs != null) {
    		if (reqs.size() == 0) {
    			fontRenderer.drawString("Free!", 4,34, 65280);
    			return;
    		}
    	   	Iterator it = reqs.entrySet().iterator();
    	   	int place = 0;
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                int type = (Integer) pairs.getKey();
                int need = (Integer) pairs.getValue();
                int have =  Minecraft.getMinecraft().thePlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(GristType.values()[type].getName());
                
                int row = place % 3;
                int col = place / 3;
                
                int color = need <= have ? 65280 : 16711680; //Green if we have enough grist, red if not
                
                fontRenderer.drawString(need + " " + GristType.values()[type].getName() + " (" + have + ")", 4 + (80 * col),34 + (8 * (row)), color);
                
                place++;
                
                //Debug.print("Need" + need + ". Have " + have);
            }
    	} else {
    		fontRenderer.drawString("Not Alchemizable", 4,34, 16711680);
    		return;
    	}

    }
    
    
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6,int w, int h) {
            float f = (float)1/w;
            float f1 = (float)1/h;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
            tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
            tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
            tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
            tessellator.draw();
    }
    
    @Override
    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction)
    {
        if(direction > 3)
        {
            completion = 1-completion;
            direction %= 4;
        }
        int var = (int) (completion * (direction % 2 == 0 ? w : h));
        
        switch(direction)
        {
            case 0://right
                this.drawTexturedModalRect(x, y, tx, ty, var, h,w,h);
            break;
            case 1://down
                this.drawTexturedModalRect(x, y, tx, ty, w, var,w,h);
            break;
            case 2://left
                this.drawTexturedModalRect(x+w-var, y, tx+w-var, ty, var, h,w,h);
            break;
            case 3://up
                this.drawTexturedModalRect(x, y+h-var, tx, ty+h-var, w, var,w,h);
            break;        
        }
    }

}
