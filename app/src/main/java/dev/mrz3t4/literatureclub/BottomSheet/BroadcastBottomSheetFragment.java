package dev.mrz3t4.literatureclub.BottomSheet;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.color.MaterialColors;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dev.mrz3t4.literatureclub.R;

public class BroadcastBottomSheetFragment extends BottomSheetDialogFragment {

    public static BroadcastBottomSheetFragment newInstance() {
        return new BroadcastBottomSheetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.bottomsheet_broadcast, container,false);
    }
}
