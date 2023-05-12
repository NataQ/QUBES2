package id.co.qualitas.qubes.fragment;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import id.co.qualitas.qubes.adapter.CaldroidCustomAdapter;

/**
 * Created by Evan on 5/12/17.
 */

public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }

}