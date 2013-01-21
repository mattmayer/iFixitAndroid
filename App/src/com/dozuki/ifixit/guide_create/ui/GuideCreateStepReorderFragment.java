package com.dozuki.ifixit.guide_create.ui;

import java.util.List;

import org.holoeverywhere.app.Fragment;

import android.R.color;
import android.os.Bundle;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dozuki.ifixit.MainApplication;
import com.dozuki.ifixit.R;
import com.dozuki.ifixit.guide_create.model.GuideCreateObject;
import com.dozuki.ifixit.guide_create.model.GuideCreateStepObject;
import com.ifixit.android.imagemanager.ImageManager;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class GuideCreateStepReorderFragment extends Fragment {

	private DragSortListView mDragListView;
	private DragSortController mController;
	private StepAdapter mAdapter;
	private ImageManager mImageManager;
	private GuideCreateObject mGuide;

	public void setGuide(GuideCreateObject guide) {
		mGuide = guide;
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			GuideCreateStepObject item = mAdapter.getItem(from);
			mAdapter.remove(item);
			mAdapter.insert(item, to);
			mDragListView.invalidateViews();
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			mAdapter.remove(mAdapter.getItem(which));
		}
	};

	/**
	 * Called in onCreateView. Override this to provide a custom
	 * DragSortController.
	 */
	public DragSortController buildController(DragSortListView dslv) {
		DragSortController controller = new DragSortController(dslv);
		controller.setDragHandleId(R.id.drag_handle_reorder);
		// controller.setClickRemoveId(R.id.click_remove);
		controller.setRemoveEnabled(false);
		controller.setSortEnabled(true);
		controller.setDragInitMode(DragSortController.ON_DOWN);
		controller.setRemoveMode(DragSortController.FLING_RIGHT_REMOVE);
		controller.setBackgroundColor(color.background_light);
		return controller;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().startActionMode(
				new ContextualStepReorder());
		if (mImageManager == null) {
			mImageManager = ((MainApplication) getActivity().getApplication())
					.getImageManager();
		}
		
		if(savedInstanceState != null)
		{
			mGuide = (GuideCreateObject)savedInstanceState.get(GuideCreateStepsActivity.GuideKey);
		}
		
		mAdapter = new StepAdapter(mGuide.getSteps());
	}
	
	@Override 
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable(GuideCreateStepsActivity.GuideKey,
				mGuide);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guide_create_step_portal_reorder,
				container, false);
		mDragListView = (DragSortListView) view
				.findViewById(R.id.steps_portal_list_reorder);
		mDragListView.setDropListener(onDrop);
		mDragListView.setRemoveListener(onRemove);
		mDragListView.setAdapter(mAdapter);
		mController = buildController(mDragListView);
		mDragListView.setFloatViewManager(mController);
		mDragListView.setOnTouchListener(mController);
		mDragListView.setDragEnabled(true);

		return view;
	}

	public final class ContextualStepReorder implements ActionMode.Callback {
		public ContextualStepReorder() {
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			getActivity().getSupportFragmentManager().popBackStack();
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			return true;
		}
	};

	private class ViewHolder {
		public TextView stepsView;
		public ImageView mImageView;
	}

	private class StepAdapter extends ArrayAdapter<GuideCreateStepObject> {
		public StepAdapter(List<GuideCreateStepObject> list) {
			super(getActivity(), R.layout.guide_create_step_list_item_reorder,
					R.id.step_title_textview_reorder, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			if (v != convertView && v != null) {
				final ViewHolder holder = new ViewHolder();

				TextView tv = (TextView) v
						.findViewById(R.id.step_title_textview_reorder);
				holder.stepsView = tv;

				holder.mImageView = (ImageView) v
						.findViewById(R.id.guide_step_item_thumbnail_reorder);
				v.setTag(holder);
			}
			final ViewHolder holder = (ViewHolder) v.getTag();
			String step = getItem(position).getTitle();
			holder.stepsView.setText(step);
			mImageManager.displayImage("", getActivity(), holder.mImageView);
			return v;
		}
	}
}