package net.ouranos_ecos.domain.repository;

import java.util.List;

import net.ouranos_ecos.domain.model.UnitElectricModel;
import net.ouranos_ecos.domain.model.UnitEnergyModel;
import net.ouranos_ecos.domain.model.UnitMaterialsModel;
import net.ouranos_ecos.domain.model.UnitModel;
import net.ouranos_ecos.domain.model.UnitPartProcessingModel;
import net.ouranos_ecos.domain.model.UnitTransportWeightModel;
import net.ouranos_ecos.domain.model.UnitWasteModel;

public interface UnitRepository {
	
	/**
	 * 原単位情報を取得
	 * @return List
	 */
	 UnitModel selectUnit();
	 
	 /**
	  * 原単位_電力を取得
	  * @param electricCd
	  * @return UnitElectricModel
	  */
	  UnitElectricModel getUnitElectric(String electricCd);
	  
	  /**
	    * 原単位_材料を取得
	    * @param lcaMaterialCd
	    * @return UnitMaterialsModel
	    */
	  UnitMaterialsModel getUnitMaterials(String lcaMaterialCd);
	  
	  /**
	    * 原単位_エネルギーを取得
	    * @return List<UnitEnergyModel>
	    */
	  List<UnitEnergyModel> getUnitEnergy();
	  
	  /**
	    * 原単位_廃棄物を取得
	    * @param wastecd
	    * @return UnitWasteModel
	    */
	  UnitWasteModel getUnitWaste(String wastecd);
	  
	  /**
	    * 原単位_輸送_重量法を取得
	    * @return List<UnitTransportWeightModel>
	    */
	  List<UnitTransportWeightModel> getUnitTransportWeight();
	  
	  /**
	    * 原単位_部品加工を取得
	    * @param partProcessingCd
	    * @return UnitPartProcessingModel
	    */
	  UnitPartProcessingModel getUnitPartProcessing(String partProcessingCd);
	 
}
