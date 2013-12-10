package com.powerdata.openpa.psse.csv;

import java.io.File;
import java.io.IOException;

import com.powerdata.openpa.psse.PsseModelException;
import com.powerdata.openpa.psse.ShuntList;
import com.powerdata.openpa.tools.LoadArray;
import com.powerdata.openpa.tools.SimpleCSV;

public class RawFixedShuntList extends ShuntList
{
	int[] _stat;
	String[] _i, _id;
	float[] _g, _b;
	int _size;
	
	public RawFixedShuntList(PsseRawModel model) throws PsseModelException
	{
		super(model);
		File dbfile = new File(model.getDir(), "FixedShunt.csv");
		try
		{
			SimpleCSV shunts = new SimpleCSV(dbfile);
			_size = shunts.getRowCount();
			_i = shunts.get("I");
			_id = LoadArray.String(shunts, "ID", this, "getDeftID");
			_stat		= LoadArray.Int(shunts,"STAT",this,"getDeftSTAT");
			_g = LoadArray.Float(shunts, "G", this, "getDeftG");
			_b = LoadArray.Float(shunts, "B", this, "getDeftB");

		} catch (IOException | ReflectiveOperationException e)
		{
			throw new PsseModelException(e);
		}
		for(int i=0; i < _size; ++i)
		{
			if (getBus(i).getIDE() == 4)
			{
				_stat[i] = 0;
			}
		}

	}
	
	public String getDeftID(int ndx) throws PsseModelException {return super.getID(ndx);}
	public int getDeftSTAT(int ndx) throws PsseModelException {return super.getSTAT(ndx);}
	public float getDeftG(int ndx) throws PsseModelException {return super.getG(ndx);}
	public float getDeftB(int ndx) throws PsseModelException {return super.getB(ndx);}
	
	@Override
	public String getI(int ndx) throws PsseModelException {return _i[ndx];}
	@Override
	public String getObjectID(int ndx) throws PsseModelException
	{
		String rv = String.format("%s-FXDSH", getBus(ndx).getObjectID());
		String id = getID(ndx);
		if (!id.isEmpty())
			rv = String.format("%s-%s", rv, id);
		return rv;
	}

	
	@Override
	public String getObjectName(int ndx) throws PsseModelException
	{
		String rv = getBus(ndx).getObjectName();
		String id = getID(ndx);
		if (!id.isEmpty())
			rv = String.format("%s-%s", rv, id);
		return rv;
	}

	@Override
	public int size() {return _size;}

	@Override
	public float getB(int ndx) throws PsseModelException {return _b[ndx];}
	@Override
	public float getG(int ndx) throws PsseModelException {return _g[ndx];}
	@Override
	public String getID(int ndx) throws PsseModelException {return _id[ndx];}
	@Override
	public int getSTAT(int ndx) throws PsseModelException {return _stat[ndx];}

}
